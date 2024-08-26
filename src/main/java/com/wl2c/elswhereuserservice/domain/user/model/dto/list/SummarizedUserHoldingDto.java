package com.wl2c.elswhereuserservice.domain.user.model.dto.list;

import com.wl2c.elswhereuserservice.client.analysis.dto.response.ResponsePriceRatioDto;
import com.wl2c.elswhereuserservice.client.product.ProductType;
import com.wl2c.elswhereuserservice.client.product.dto.response.ResponseSummarizedProductForHoldingDto;
import com.wl2c.elswhereuserservice.domain.user.model.entity.Holding;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class SummarizedUserHoldingDto {

    @Schema(description = "사용자 보유 상품 id", example = "1")
    private final Long holdingId;

    @Schema(description = "상품 id", example = "3")
    private final Long productId;

    @Schema(description = "발행 회사", example = "oo투자증권")
    private final String issuer;

    @Schema(description = "상품명", example = "oo투자증권 99999")
    private final String name;

    @Schema(description = "상품 유형", example = "STEP_DOWN or LIZARD or MONTHLY_PAYMENT or ETC")
    private final ProductType productType;

    @Schema(description = "수익률", example = "20.55")
    private final BigDecimal yieldIfConditionsMet;

    @Schema(description = "다음 상환평가일", example = "2027-01-13")
    private final LocalDate nextRepaymentEvaluationDate;

    @Schema(description = "투자 금액", example = "1000000")
    private final BigDecimal price;

    @Schema(description = "최초기준가격 대비 최근 기초자산가격 비율(%)", example = "-8.32")
    private final BigDecimal recentAndInitialPriceRatio;

    public SummarizedUserHoldingDto(@NonNull Holding holding,
                                    ResponseSummarizedProductForHoldingDto responseSummarizedProductForHoldingDto,
                                    ResponsePriceRatioDto responsePriceRatioDto) {
        this.holdingId = holding.getId();
        this.price = holding.getPrice();
        this.productId = holding.getProductId();
        this.issuer = responseSummarizedProductForHoldingDto.getIssuer();
        this.name = responseSummarizedProductForHoldingDto.getName();
        this.productType = responseSummarizedProductForHoldingDto.getProductType();
        this.yieldIfConditionsMet = responseSummarizedProductForHoldingDto.getYieldIfConditionsMet();
        this.nextRepaymentEvaluationDate = responseSummarizedProductForHoldingDto.getNextRepaymentEvaluationDate();
        this.recentAndInitialPriceRatio = (responsePriceRatioDto == null) ? null : responsePriceRatioDto.getRecentAndInitialPriceRatio();
    }
}
