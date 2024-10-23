package com.wl2c.elswhereuserservice.domain.oauth.google.service;

import com.fasterxml.jackson.databind.JsonNode;
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
import com.wl2c.elswhereuserservice.global.auth.jwt.JwtProvider;
import com.wl2c.elswhereuserservice.global.auth.role.UserRole;
import com.wl2c.elswhereuserservice.global.error.exception.FailedOAuthCallbackProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuth2V2Service {

    @Value("${oauth2.google.client-id}")
    private String googleClientId;

    @Value("${oauth2.google.client-secret}")
    private String googleClientSecret;

    @Value("${oauth2.google.redirect-uri-v2}")
    private String googleRedirectUriV2;

    @Value("${oauth2.google.token-uri}")
    private String googleTokenUri;

    @Value("${oauth2.google.user-info-uri}")
    private String googleUserInfoUri;

    @Value("${oauth2.google.authorization-uri}")
    private String googleAuthorizationUri;

    public static final String OAUTH_AUTH_NAME = "oauth";

    private final Clock clock;

    private final UserRepository userRepository;
    private final SignupAuthRepository signupAuthRepository;
    private final JwtProvider jwtProvider;

    private final UserService userService;
    private final UserInfoService userInfoService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional(readOnly = true)
    public ResponseEntity<String> handleGoogleOAuthCallback(Map<String, String> params, HttpServletResponse response) {

        String code = params.get("code");
        if (params.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.SEE_OTHER).header("Location", "elswhere://?error=access_denied").build();
        }

        try {
            // 구글로부터 토큰(엑세스, 리프레쉬) 요청
            Map<String, String> tokenRequest = new HashMap<>();
            tokenRequest.put("code", code);
            tokenRequest.put("client_id", googleClientId);
            tokenRequest.put("client_secret", googleClientSecret);
            tokenRequest.put("redirect_uri", googleRedirectUriV2);
            tokenRequest.put("grant_type", "authorization_code");

            ResponseEntity<JsonNode> tokenResponse = restTemplate.postForEntity(googleTokenUri, tokenRequest, JsonNode.class);

            if (tokenResponse.getStatusCode().is2xxSuccessful()) {
                String accessToken = Objects.requireNonNull(tokenResponse.getBody()).get("access_token").asText();
                String refreshToken = tokenResponse.getBody().get("refresh_token").asText();

                Map<String, Object> userInfo = null;
                try {
                    ResponseEntity<Map<String, Object>> userInfoResponse = restTemplate.exchange(
                            googleUserInfoUri + "?access_token=" + accessToken,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<Map<String, Object>>() {}
                    );
                    userInfo = userInfoResponse.getBody();

                } catch (RestClientException e) {
                    e.printStackTrace();
                }

                Optional<User> optionalUser = userRepository.findBySocialId(userInfo.get("sub").toString());
                User user;
                if (optionalUser.isEmpty()) {
                    user = User.builder()
                            .socialId(userInfo.get("sub").toString())
                            .socialType(SocialType.GOOGLE)
                            .email(userInfo.get("email").toString())
                            .name(userInfo.get("name").toString())
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

    public String getAuthorizationUri() {
        return googleAuthorizationUri
                + "?client_id=" + googleClientId
                + "&redirect_uri=" + googleRedirectUriV2
                + "&response_type=code"
                + "&scope=profile email"
                + "&access_type=offline"
                + "&prompt=consent";
    }
}
