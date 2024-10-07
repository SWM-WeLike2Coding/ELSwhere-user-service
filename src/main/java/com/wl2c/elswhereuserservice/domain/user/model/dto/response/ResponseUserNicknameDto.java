package com.wl2c.elswhereuserservice.domain.user.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ResponseUserNicknameDto {

    @Schema(description = "닉네임", example = "홍길동")
    private final String nickname;

    public ResponseUserNicknameDto(String nickname) {
        this.nickname = nickname;
    }

}
