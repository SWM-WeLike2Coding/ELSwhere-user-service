package com.wl2c.elswhereuserservice.domain.user.model.dto.request;

import com.wl2c.elswhereuserservice.domain.user.model.InvestmentExperience;
import com.wl2c.elswhereuserservice.domain.user.model.InvestmentPreferredPeriod;
import com.wl2c.elswhereuserservice.domain.user.model.RiskTakingAbility;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RequestCreateInvestmentPropensityDto {

    @Schema(description = "ELS 상품 투자 경험", example = "YES")
    private final InvestmentExperience investmentExperience;

    @Schema(description = "투자 선호 기간", example = "LESS_THAN_A_YEAR")
    private final InvestmentPreferredPeriod investmentPreferredPeriod;

    @Schema(description = "투자자 위험 감수 능력", example = "RISK_TAKING_TYPE")
    private final RiskTakingAbility riskTakingAbility;
}
