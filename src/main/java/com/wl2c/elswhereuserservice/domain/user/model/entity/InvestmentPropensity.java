package com.wl2c.elswhereuserservice.domain.user.model.entity;

import com.wl2c.elswhereuserservice.domain.user.model.InvestmentExperience;
import com.wl2c.elswhereuserservice.domain.user.model.RepaymentOption;
import com.wl2c.elswhereuserservice.domain.user.model.RiskPropensity;
import com.wl2c.elswhereuserservice.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InvestmentPropensity extends BaseEntity {

    @Id
    @Column(name = "investment_propensity_id", nullable = false)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "investment_propensity_id", referencedColumnName = "investment_propensity_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Enumerated(STRING)
    private InvestmentExperience investmentExperience;

    @Enumerated(STRING)
    private RiskPropensity riskPropensity;

    @Enumerated(STRING)
    private RepaymentOption repaymentOption;

    private BigDecimal minPreferredReturn;

    @Builder
    public InvestmentPropensity(User user,
                                InvestmentExperience investmentExperience,
                                RiskPropensity riskPropensity,
                                RepaymentOption repaymentOption,
                                BigDecimal minPreferredReturn) {
        this.user = user;
        this.investmentExperience = investmentExperience;
        this.riskPropensity = riskPropensity;
        this.repaymentOption = repaymentOption;
        this.minPreferredReturn = minPreferredReturn;
    }

    public void changeInvestmentExperience(InvestmentExperience investmentExperience) {
        this.investmentExperience = investmentExperience;
    }

    public void changeRiskPropensity(RiskPropensity riskPropensity) {
        this.riskPropensity = riskPropensity;
    }

    public void changeRepaymentOption(RepaymentOption repaymentOption) {
        this.repaymentOption = repaymentOption;
    }

    public void changeMinPreferredReturn(BigDecimal minPreferredReturn) {
        this.minPreferredReturn = minPreferredReturn;
    }

}
