package com.wl2c.elswhereuserservice.domain.user.model.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;


@Getter
public class RequestNickNameChangeDto {
    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^(?!.*\\s{2,})[A-Za-z\\dㄱ-ㅎㅏ-ㅣ가-힣_ ]{3,16}$")
    @Schema(description = "새로운 닉네임", example = "돈을 잃지 말자")
    private final String nickname;

    @JsonCreator
    public RequestNickNameChangeDto(String nickname) {
        this.nickname = nickname;
    }
}
