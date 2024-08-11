package com.wl2c.elswhereuserservice.domain.user.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class RequestRefreshTokenDto {

    @NotBlank
    private String refreshToken;

    public RequestRefreshTokenDto() {}

    public RequestRefreshTokenDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
