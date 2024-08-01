package com.wl2c.elswhereuserservice.domain.user.model.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class RequestCreateInterestDto {

    @Schema(description = "상품 id", example = "3")
    private final Long productId;

    @JsonCreator
    public RequestCreateInterestDto(@JsonProperty("productId") Long productId) {
        this.productId = productId;
    }
}
