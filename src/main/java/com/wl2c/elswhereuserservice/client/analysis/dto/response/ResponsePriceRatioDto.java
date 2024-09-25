package com.wl2c.elswhereuserservice.client.analysis.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ResponsePriceRatioDto {

    @Schema(description = "상품 id", example = "1")
    private final Long id;

    @Schema(description = "최초기준가격 대비 최근 기초자산가격 비율(%)", example = "-8.32")
    private final BigDecimal recentAndInitialPriceRatio;
}
