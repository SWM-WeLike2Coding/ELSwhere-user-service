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
        return appleOAuth2Service.handleAppleOAuthCallback(params, response);
    }
}
