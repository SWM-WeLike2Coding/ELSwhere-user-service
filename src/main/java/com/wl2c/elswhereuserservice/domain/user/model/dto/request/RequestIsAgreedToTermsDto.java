package com.wl2c.elswhereuserservice.domain.user.model.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RequestIsAgreedToTermsDto {

    @Schema(description = "회원가입을 진행하는 사용자의 서비스 이용 약관 동의 여부", example = "true")
    private final boolean agreed;

    @JsonCreator
    public RequestIsAgreedToTermsDto(@JsonProperty("agreed") boolean agreed) {
        this.agreed = agreed;
    }

}
