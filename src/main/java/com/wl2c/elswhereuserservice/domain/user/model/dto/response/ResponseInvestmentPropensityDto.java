package com.wl2c.elswhereuserservice.domain.user.model.dto.response;

import com.wl2c.elswhereuserservice.domain.user.model.InvestmentExperience;
import com.wl2c.elswhereuserservice.domain.user.model.RepaymentOption;
import com.wl2c.elswhereuserservice.domain.user.model.RiskPropensity;
import com.wl2c.elswhereuserservice.domain.user.model.entity.InvestmentPropensity;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ResponseInvestmentPropensityDto {

    private final InvestmentExperience investmentExperience;

    private final RiskPropensity riskPropensity;

    private final RepaymentOption repaymentOption;

    private final BigDecimal minPreferredReturn;

    public ResponseInvestmentPropensityDto(InvestmentPropensity investmentPropensity) {
        this.investmentExperience = investmentPropensity.getInvestmentExperience();
        this.riskPropensity = investmentPropensity.getRiskPropensity();
        this.repaymentOption = investmentPropensity.getRepaymentOption();
        this.minPreferredReturn = investmentPropensity.getMinPreferredReturn();
    }
}
