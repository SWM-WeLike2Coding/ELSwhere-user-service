package com.wl2c.elswhereuserservice.domain.oauth.apple.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wl2c.elswhereuserservice.domain.oauth.apple.exception.FailedToReceiveAppleOAuth2TokenException;
import com.wl2c.elswhereuserservice.domain.oauth.apple.jwt.AppleJwtTokenProvider;
import com.wl2c.elswhereuserservice.domain.user.model.SocialType;
import com.wl2c.elswhereuserservice.domain.user.model.UserStatus;
import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import com.wl2c.elswhereuserservice.domain.user.repository.UserRepository;
import com.wl2c.elswhereuserservice.domain.user.service.UserInfoService;
import com.wl2c.elswhereuserservice.domain.user.service.UserService;
import com.wl2c.elswhereuserservice.global.auth.jwt.AuthenticationToken;
import com.wl2c.elswhereuserservice.global.auth.jwt.JwtDecoder;
import com.wl2c.elswhereuserservice.global.auth.jwt.JwtProvider;
import com.wl2c.elswhereuserservice.global.auth.role.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppleOAuth2Service {

    @Value("${oauth2.apple.client-id}")
    private String appleClientId;

    @Value("${oauth2.apple.key-id}")
    private String appleKeyId;

    @Value("${oauth2.apple.team-id}")
    private String appleTeamId;

    @Value("${oauth2.apple.redirect-uri}")
    private String appleRedirectUri;

    @Value("${oauth2.apple.key-path}")
    private String appleKeyPath;

    @Value("${oauth2.apple.authorization-uri}")
    private String appleAuthorizationUri;

    @Value("${oauth2.apple.token-uri}")
    private String appleTokenUri;

    @Value("${oauth2.apple.revoke-token-uri}")
    private String appleRevokeTokenUri;

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    private final UserService userService;
    private final UserInfoService userInfoService;
    private final AppleJwtTokenProvider tokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public ResponseEntity<?> handleAppleOAuthCallback(Map<String, Object> params, HttpServletResponse response) throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("elswhere://");

        if (params.containsKey("error")) {
            String redirectUrl = builder.queryParam("error", params.get("error")).build().toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", redirectUrl);
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }
        // Apple OAuth 서비스로부터 액세스 토큰을 가져옴
        Map<String, String> tokenResponse = getTokens(params, response);
        if (tokenResponse.containsKey("error")) {
            String error = tokenResponse.get("error");
            String redirectUrl = builder.queryParam("error", error).build().toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", redirectUrl);
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }
        String accessToken = tokenResponse.get("access_token");
        String refreshToken = tokenResponse.get("refresh_token");

        // 클라이언트 앱의 콜백 URL 스킴을 사용하여 리디렉션
        String callbackUrl = builder
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .build().toUriString();

        // 리디렉션을 위한 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", callbackUrl);

        // 302 리디렉션 응답
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @Transactional
    public Map<String, String> getTokens(Map<String, Object> params, HttpServletResponse httpServletResponse) throws Exception {

        // 최초 인증 시에만 이름이 옴
        String authorizationCode = params.get("code").toString();
        String firstName;
        String lastName;
        String fullName = "";
        try {

            // 'userJson'을 JSON으로 변환
            Map<String, Object> userMap = objectMapper.readValue(params.get("user").toString(), Map.class);

            // 'userMap'에서 사용자의 이름과 이메일 추출
            if (!userMap.isEmpty()) {
                firstName = (String) ((Map<String, Object>) userMap.get("name")).get("firstName");
                lastName = (String) ((Map<String, Object>) userMap.get("name")).get("lastName");
                fullName = (lastName + " " + firstName).trim();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Apple OAuth 클라이언트 ID, 팀 ID, 키 ID 및 비밀 키
        String clientId = appleClientId;
        String teamId = appleTeamId;
        String keyId = appleKeyId;
        String privateKeyPath = appleKeyPath;
        String tokenUri = appleTokenUri;
        String redirectUri = appleRedirectUri;

        // JWT 생성
        String clientSecret = tokenProvider.createJwtToken(clientId, teamId, keyId, privateKeyPath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Apple에 토큰 요청
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("code", authorizationCode); // 클라이언트에서 받은 authorization_code
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", redirectUri);  // 콜백 URL

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        Map<String, String> result = null;
        Map<String, Object> userInfo = null;
        try {
            ResponseEntity<Map> response = restTemplate.exchange(tokenUri, HttpMethod.POST, request, Map.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                // Apple 토큰 서버에 POST 요청
                Map<String, String> tokenResponse = response.getBody();

                Map<String, Object> decodedJWT = JwtDecoder.decode(tokenResponse.get("id_token"));
                userInfo = decodedJWT;
                try {
                    Optional<User> optionalUser = userRepository.findBySocialId(userInfo.get("sub").toString());
                    User user;
                    if (optionalUser.isEmpty()) {
                        user = User.builder()
                                .socialId(userInfo.get("sub").toString())
                                .socialType(SocialType.APPLE)
                                .email(userInfo.get("email").toString())
                                .name(fullName)
                                .nickname(userService.createRandomNickname())
                                .userStatus(UserStatus.ACTIVE)
                                .userRole(UserRole.USER)
                                .build();
                        userRepository.save(user);
                    } else {
                        user = optionalUser.get();
                    }
                    // 백엔드 서버에서 서비스를 위한 자체 토큰 발급
                    AuthenticationToken token = jwtProvider.issue(user);
                    userInfoService.cacheUserInfo(user.getId(), user);

                    result = new HashMap<>();
                    result.put("access_token", token.getAccessToken());
                    result.put("refresh_token", token.getRefreshToken());

                    return result;

                } catch (Exception e) {
                    httpServletResponse.sendRedirect("elswhere://?error=invalid_token");
                    throw new FailedToReceiveAppleOAuth2TokenException();
                }
            } else {
                httpServletResponse.sendRedirect("elswhere://?error=invalid_token");
                throw new FailedToReceiveAppleOAuth2TokenException();
            }
        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }

    public String getAuthorizationUri() {
        return appleAuthorizationUri
                + "?client_id=" + appleClientId
                + "&redirect_uri=" + appleRedirectUri
                + "&response_type=code"
                + "&response_mode=form_post"
                + "&scope=name email";
    }
}
