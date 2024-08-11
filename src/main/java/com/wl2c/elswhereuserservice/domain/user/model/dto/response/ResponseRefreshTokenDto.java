package com.wl2c.elswhereuserservice.domain.user.model.dto.response;

import com.wl2c.elswhereuserservice.global.auth.jwt.AuthenticationToken;
import lombok.Getter;

@Getter
public class ResponseRefreshTokenDto {
    private final String accessToken;
    private final String refreshToken;

    public ResponseRefreshTokenDto(AuthenticationToken token) {
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
    }
}
