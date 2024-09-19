package com.wl2c.elswhereuserservice.domain.oauth.apple.controller;

import com.wl2c.elswhereuserservice.domain.oauth.apple.service.AppleOAuth2Service;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Tag(name = "애플 OAuth", description = "애플 OAuth 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oauth2/apple")
public class AppleOAuth2Controller {
    private static final Logger log = LoggerFactory.getLogger(AppleOAuth2Controller.class);
    private final AppleOAuth2Service appleOAuth2Service;

    // Apple OAuth 인증 페이지로 리디렉션하는 엔드포인트
    @GetMapping("/login")
    public void redirectToAppleOAuth(HttpServletResponse response) throws IOException {
        String authorizationUri = appleOAuth2Service.getAuthorizationUri();
        response.sendRedirect(authorizationUri);
    }

    // Apple OAuth에서 인증 후 리디렉션될 콜백 엔드포인트
    @PostMapping("/callback")
    public ResponseEntity<?> handleAppleOAuthCallback(@RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        log.info(params.toString());
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("elswhere://");

        if (params.containsKey("error")) {
            String redirectUrl = builder.queryParam("error", params.get("error")).build().toUriString();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", redirectUrl);
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }
        // Apple OAuth 서비스로부터 액세스 토큰을 가져옴
        Map<String, String> tokenResponse = appleOAuth2Service.getTokens(params, response);
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
}
