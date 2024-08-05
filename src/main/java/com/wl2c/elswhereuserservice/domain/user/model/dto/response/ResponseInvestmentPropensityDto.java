package com.wl2c.elswhereuserservice.domain.user.model.dto.response;

import com.wl2c.elswhereuserservice.domain.user.model.InvestmentExperience;
import com.wl2c.elswhereuserservice.domain.user.model.InvestmentPreferredPeriod;
import com.wl2c.elswhereuserservice.domain.user.model.RiskTakingAbility;
import com.wl2c.elswhereuserservice.domain.user.model.entity.InvestmentPropensity;
import lombok.Getter;

@Getter
public class ResponseInvestmentPropensityDto {

    private final InvestmentExperience investmentExperience;

    private final InvestmentPreferredPeriod investmentPreferredPeriod;

    private final RiskTakingAbility riskTakingAbility;

    public ResponseInvestmentPropensityDto(InvestmentPropensity investmentPropensity) {
        this.investmentExperience = investmentPropensity.getInvestmentExperience();
        this.investmentPreferredPeriod = investmentPropensity.getInvestmentPreferredPeriod();
        this.riskTakingAbility = investmentPropensity.getRiskTakingAbility();
    }
}
