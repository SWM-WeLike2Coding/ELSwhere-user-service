package com.wl2c.elswhereuserservice.domain.oauth.google.controller;

import com.wl2c.elswhereuserservice.domain.oauth.google.service.GoogleOAuth2Service;
import com.wl2c.elswhereuserservice.domain.user.model.dto.response.ResponseLoginDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "구글 OAuth", description = "구글 OAuth 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oauth2/google")
public class GoogleOAuth2Controller {

    private final GoogleOAuth2Service googleOAuth2Service;

    @GetMapping("/callback")
    public ResponseLoginDto callback(@RequestParam("code") String code) {
        return googleOAuth2Service.processCallback(code);
    }

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        String authorizationUri = googleOAuth2Service.getAuthorizationUri();
        response.sendRedirect(authorizationUri);
    }
}
