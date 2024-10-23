package com.wl2c.elswhereuserservice.domain.oauth.google.controller;

import com.wl2c.elswhereuserservice.domain.oauth.google.service.GoogleOAuth2V2Service;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@Tag(name = "구글 OAuth", description = "구글 OAuth 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/oauth2/google/")
public class GoogleOAuth2V2Controller {

    private final GoogleOAuth2V2Service googleOAuth2V2Service;

    /**
     * Google OAuth 인증 페이지로 리다이렉션하는 엔드포인트 v2
     */
    @GetMapping("/login")
    public void loginV2(HttpServletResponse response) throws IOException {
        String authorizationUri = googleOAuth2V2Service.getAuthorizationUri();
        response.sendRedirect(authorizationUri);
    }

    /**
     * Google OAuth에서 인증 후 리다이렉션될 콜백 엔드포인트 v2
     */
    @GetMapping("/callback")
    public ResponseEntity<String> callbackV2(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        return googleOAuth2V2Service.handleGoogleOAuthCallback(params, response);
    }

}
