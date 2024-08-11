package com.wl2c.elswhereuserservice.domain.user.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class RequestCreateHoldingDto {

    @Schema(description = "상품 id", example = "3")
    private final Long productId;

    @Schema(description = "가격", example = "50000")
    private final BigDecimal price;
}
