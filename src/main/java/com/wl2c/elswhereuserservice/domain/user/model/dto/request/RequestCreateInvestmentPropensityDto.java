package com.wl2c.elswhereuserservice.domain.user.model.dto.request;

import com.wl2c.elswhereuserservice.domain.user.model.InvestmentExperience;
import com.wl2c.elswhereuserservice.domain.user.model.RepaymentOption;
import com.wl2c.elswhereuserservice.domain.user.model.RiskPropensity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class RequestCreateInvestmentPropensityDto {

    @Schema(description = "ELS 상품 투자 경험", example = "YES")
    private final InvestmentExperience investmentExperience;

    @Schema(description = "투자자 위험 감수 능력", example = "MEDIUM_RISK")
    private final RiskPropensity riskPropensity;

    @Schema(description = "희망 상환 기간", example = "EARLY_REPAYMENT")
    private final RepaymentOption repaymentOption;

    @Schema(description = "선호 최소 수익률(연%)", example = "8.4")
    private final BigDecimal minPreferredReturn;

}
