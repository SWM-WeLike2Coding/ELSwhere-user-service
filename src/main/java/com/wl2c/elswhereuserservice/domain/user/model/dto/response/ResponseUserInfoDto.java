package com.wl2c.elswhereuserservice.domain.user.model.dto.response;

import com.wl2c.elswhereuserservice.domain.user.model.SocialType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ResponseUserInfoDto {

    private final LocalDateTime createdAt;
    private final SocialType socialType;
    private final String email;
    private final String name;
    private final String nickname;
    private final boolean admin;
}
