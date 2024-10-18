package com.wl2c.elswhereuserservice.domain.oauth.apple.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wl2c.elswhereuserservice.domain.oauth.apple.jwt.AppleJwtTokenProvider;
import com.wl2c.elswhereuserservice.domain.oauth.google.exception.FailedToReceiveGoogleOAuth2TokenException;
import com.wl2c.elswhereuserservice.domain.user.model.SocialType;
import com.wl2c.elswhereuserservice.domain.user.model.UserStatus;
import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import com.wl2c.elswhereuserservice.domain.user.repository.SignupAuthRepository;
import com.wl2c.elswhereuserservice.domain.user.repository.UserRepository;
import com.wl2c.elswhereuserservice.domain.user.service.UserInfoService;
import com.wl2c.elswhereuserservice.domain.user.service.UserService;
import com.wl2c.elswhereuserservice.domain.user.util.CodeGenerator;
import com.wl2c.elswhereuserservice.global.auth.jwt.AuthenticationToken;
import com.wl2c.elswhereuserservice.global.auth.jwt.JwtDecoder;
import com.wl2c.elswhereuserservice.global.auth.jwt.JwtProvider;
import com.wl2c.elswhereuserservice.global.auth.role.UserRole;
import com.wl2c.elswhereuserservice.global.error.exception.FailedOAuthCallbackProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppleOAuth2V2Service {

    @Value("${oauth2.apple.client-id}")
    private String appleClientId;

    @Value("${oauth2.apple.key-id}")
    private String appleKeyId;

    @Value("${oauth2.apple.team-id}")
    private String appleTeamId;

    @Value("${oauth2.apple.redirect-uri-v2}")
    private String appleRedirectUriV2;

    @Value("${oauth2.apple.key-path}")
    private String appleKeyPath;

    @Value("${oauth2.apple.authorization-uri}")
    private String appleAuthorizationUri;

    @Value("${oauth2.apple.token-uri}")
    private String appleTokenUri;

    @Value("${oauth2.apple.revoke-token-uri}")
    private String appleRevokeTokenUri;

    private String fullName;

    public static final String OAUTH_AUTH_NAME = "oauth";

    private final Clock clock;

    private final UserRepository userRepository;
    private final SignupAuthRepository signupAuthRepository;
    private final JwtProvider jwtProvider;

    private final UserService userService;
    private final UserInfoService userInfoService;
    private final AppleJwtTokenProvider appleJwtTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional(readOnly = true)
    public ResponseEntity<String> handleAppleOAuthCallback(Map<String, Object> params, HttpServletResponse response) throws Exception {

        if (params.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.SEE_OTHER).header("Location", "elswhere://?error=access_denied").build();
        }

        try {
            HttpEntity<MultiValueMap<String, String>> request = createAppleAuthTokenRequest(params);
            ResponseEntity<Map> tokenResponse = restTemplate.exchange(appleTokenUri, HttpMethod.POST, request, Map.class);

            if (tokenResponse.getStatusCode().is2xxSuccessful()) {

                Map<String, Object> userInfo = JwtDecoder.decode((String) tokenResponse.getBody().get("id_token"));

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

                    String signupToken = CodeGenerator.generateUUIDCode();
                    signupAuthRepository.setAuthPayload(signupToken, OAUTH_AUTH_NAME, user, Instant.now(clock));

                    // 서비스 이용 약관 페이지로 리다이렉션
                    String redirectUrl = "elswhere://terms?signup_token=" + signupToken;
                    return ResponseEntity.status(302).header("Location", redirectUrl).build();
                } else {
                    user = optionalUser.get();

                    // 백엔드 서버에서 서비스를 위한 자체 토큰 발급
                    AuthenticationToken token = jwtProvider.issue(user);
                    userInfoService.cacheUserInfo(user.getId(), user);

                    String redirectUrl = "elswhere://callback?access_token=" + token.getAccessToken() + "&refresh_token=" + token.getRefreshToken();
                    return ResponseEntity.status(302).header("Location", redirectUrl).build();
                }
            } else {
                response.sendRedirect("elswhere://?error=invalid_token");
                throw new FailedToReceiveGoogleOAuth2TokenException();
            }
        } catch (Exception e) {
            try {
                response.sendRedirect("elswhere://?error=invalid_token");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new FailedOAuthCallbackProcessingException(e);
        }
    }

    private HttpEntity<MultiValueMap<String, String>> createAppleAuthTokenRequest(Map<String, Object> params) throws Exception {

        // 최초 인증 시에만 이름이 옴
        String authorizationCode = params.get("code").toString();
        String firstName;
        String lastName;

        // 'userJson'을 JSON으로 변환
        Map<String, Object> userMap;
        if (Objects.nonNull(params.get("user"))) {
            userMap = objectMapper.readValue(params.get("user").toString(), Map.class);

            // 'userMap'에서 사용자의 이름과 이메일 추출
            if (!userMap.isEmpty()) {
                Map<String, Object> nameMap = (Map<String, Object>) userMap.get("name");
                if (Objects.nonNull(nameMap)) {
                    firstName = (String) nameMap.get("firstName");
                    lastName = (String) nameMap.get("lastName");
                    fullName = (lastName + " " + firstName).trim();
                }
            }
        }

        // JWT 생성
        String clientSecret = appleJwtTokenProvider.createJwtToken(appleClientId, appleTeamId, appleKeyId, appleKeyPath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Apple에 토큰 요청
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", appleClientId);
        body.add("client_secret", clientSecret);
        body.add("code", authorizationCode); // 클라이언트에서 받은 authorization_code
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", appleRedirectUriV2);  // 콜백 URL

        return new HttpEntity<>(body, headers);
    }

    public String getAuthorizationUri() {
        return appleAuthorizationUri
                + "?client_id=" + appleClientId
                + "&redirect_uri=" + appleRedirectUriV2
                + "&response_type=code"
                + "&response_mode=form_post"
                + "&scope=name email";
    }
}