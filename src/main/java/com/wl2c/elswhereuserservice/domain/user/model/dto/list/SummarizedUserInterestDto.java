package com.wl2c.elswhereuserservice.domain.user.model.dto.list;

import com.wl2c.elswhereuserservice.client.product.ProductType;
import com.wl2c.elswhereuserservice.client.product.dto.response.ResponseSummarizedProductDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class SummarizedUserInterestDto {

    @Schema(description = "사용자 관심 상품 id", example = "1")
    private final Long interestId;

    @Schema(description = "상품 id", example = "3")
    private final Long productId;

    @Schema(description = "발행 회사", example = "oo투자증권")
    private final String issuer;

    @Schema(description = "상품명", example = "oo투자증권 99999")
    private final String name;

    @Schema(description = "상품 유형", example = "STEP_DOWN or LIZARD or MONTHLY_PAYMENT or ETC")
    private final ProductType productType;

    @Schema(description = "기초자산", example = "KOSPI200 Index / HSCEI Index / S&P500 Index")
    private final String equities;

    @Schema(description = "수익률", example = "20.55")
    private final BigDecimal yieldIfConditionsMet;

    @Schema(description = "낙인 값", example = "45, 낙인 값이 없을 시 null return")
    private final Integer knockIn;

    @Schema(description = "청약 시작일", example = "2024-06-14")
    private final LocalDate subscriptionStartDate;

    @Schema(description = "청약 마감일", example = "2024-06-21")
    private final LocalDate subscriptionEndDate;

    @Schema(description = "AI가 판단한 스텝다운 상품 안전도", example = "0.89")
    private final BigDecimal safetyScore;

    public SummarizedUserInterestDto(@NonNull Long interestId,
                                     @NonNull ResponseSummarizedProductDto responseSummarizedProductDto) {
        this.interestId = interestId;
        this.productId = responseSummarizedProductDto.getId();
        this.issuer = responseSummarizedProductDto.getIssuer();
        this.name = responseSummarizedProductDto.getName();
        this.productType = responseSummarizedProductDto.getProductType();
        this.equities = responseSummarizedProductDto.getEquities();
        this.yieldIfConditionsMet = responseSummarizedProductDto.getYieldIfConditionsMet();
        this.knockIn = responseSummarizedProductDto.getKnockIn();
        this.subscriptionStartDate = responseSummarizedProductDto.getSubscriptionStartDate();
        this.subscriptionEndDate = responseSummarizedProductDto.getSubscriptionEndDate();
        this.safetyScore = responseSummarizedProductDto.getSafetyScore();
    }

}
