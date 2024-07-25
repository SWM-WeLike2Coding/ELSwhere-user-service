package com.wl2c.elswhereuserservice.domain.oauth.google.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wl2c.elswhereuserservice.domain.oauth.google.exception.FailedToReceiveGoogleOAuth2TokenException;
import com.wl2c.elswhereuserservice.domain.user.exception.UserNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.model.SocialType;
import com.wl2c.elswhereuserservice.domain.user.model.UserStatus;
import com.wl2c.elswhereuserservice.domain.user.model.dto.response.ResponseLoginDto;
import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import com.wl2c.elswhereuserservice.domain.user.repository.UserRepository;
import com.wl2c.elswhereuserservice.domain.user.service.UserInfoService;
import com.wl2c.elswhereuserservice.domain.user.service.UserService;
import com.wl2c.elswhereuserservice.global.auth.jwt.AuthenticationToken;
import com.wl2c.elswhereuserservice.global.auth.jwt.JwtProvider;
import com.wl2c.elswhereuserservice.global.auth.role.UserRole;
import com.wl2c.elswhereuserservice.global.exception.FailedOAuthCallbackProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuth2Service {

    @Value("${oauth2.google.client-id}")
    private String googleClientId;

    @Value("${oauth2.google.client-secret}")
    private String googleClientSecret;

    @Value("${oauth2.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${oauth2.google.token-uri}")
    private String googleTokenUri;

    @Value("${oauth2.google.user-info-uri}")
    private String googleUserInfoUri;

    @Value("${oauth2.google.authorization-uri}")
    private String googleAuthorizationUri;

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    private final UserService userService;
    private final UserInfoService userInfoService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public ResponseLoginDto processCallback(String code) {
        try {
            // 구글로부터 토큰(엑세스, 리프레쉬) 요청
            Map<String, String> tokenRequest = new HashMap<>();
            tokenRequest.put("code", code);
            tokenRequest.put("client_id", googleClientId);
            tokenRequest.put("client_secret", googleClientSecret);
            tokenRequest.put("redirect_uri", googleRedirectUri);
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
                    userRepository.save(user);
                } else {
                    user = optionalUser.get();
                }
                AuthenticationToken token = jwtProvider.issue(user);
                userInfoService.cacheUserInfo(user.getId(), user);

                return new ResponseLoginDto(token);

            } else {
                throw new FailedToReceiveGoogleOAuth2TokenException();
            }
        } catch (Exception e) {
            throw new FailedOAuthCallbackProcessingException(e);
        }
    }

    public String getAuthorizationUri() {
        return googleAuthorizationUri
                + "?client_id=" + googleClientId
                + "&redirect_uri=" + googleRedirectUri
                + "&response_type=code"
                + "&scope=profile email"
                + "&access_type=offline"
                + "&prompt=consent";
    }
}
