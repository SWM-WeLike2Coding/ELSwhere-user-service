package com.wl2c.elswhereuserservice.domain.user.model.dto.response;

import com.wl2c.elswhereuserservice.domain.user.model.SocialType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseUserInfoDto {

    private final SocialType socialType;
    private final String email;
    private final String name;
    private final String nickname;
    private final boolean admin;
}
