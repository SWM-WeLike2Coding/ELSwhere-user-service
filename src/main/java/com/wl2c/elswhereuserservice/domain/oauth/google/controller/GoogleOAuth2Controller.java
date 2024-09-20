package com.wl2c.elswhereuserservice.domain.oauth.google.controller;

import com.wl2c.elswhereuserservice.domain.oauth.google.service.GoogleOAuth2Service;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Tag(name = "구글 OAuth", description = "구글 OAuth 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oauth2/google")
public class GoogleOAuth2Controller {

    private final GoogleOAuth2Service googleOAuth2Service;

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam Map<String, String> param, HttpServletResponse response) throws IOException {
        String code = param.get("code");
        String error = param.get("error");
        if (param.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.SEE_OTHER).header("Location", "elswhere://?error=access_denied").build();
        } else {
            return googleOAuth2Service.processCallback(code, response);
        }
    }

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        String authorizationUri = googleOAuth2Service.getAuthorizationUri();
        response.sendRedirect(authorizationUri);
    }
}
