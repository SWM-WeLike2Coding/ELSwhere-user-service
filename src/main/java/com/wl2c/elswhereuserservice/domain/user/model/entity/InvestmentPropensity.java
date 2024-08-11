package com.wl2c.elswhereuserservice.domain.user.model.entity;

import com.wl2c.elswhereuserservice.domain.user.model.InvestmentExperience;
import com.wl2c.elswhereuserservice.domain.user.model.InvestmentPreferredPeriod;
import com.wl2c.elswhereuserservice.domain.user.model.RiskTakingAbility;
import com.wl2c.elswhereuserservice.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private InvestmentPreferredPeriod investmentPreferredPeriod;

    @Enumerated(STRING)
    private RiskTakingAbility riskTakingAbility;

    @Builder
    public InvestmentPropensity(User user,
                                InvestmentExperience investmentExperience,
                                InvestmentPreferredPeriod investmentPreferredPeriod,
                                RiskTakingAbility riskTakingAbility) {
        this.user = user;
        this.investmentExperience = investmentExperience;
        this.investmentPreferredPeriod = investmentPreferredPeriod;
        this.riskTakingAbility = riskTakingAbility;
    }

    public void changeInvestmentExperience(InvestmentExperience investmentExperience) {
        this.investmentExperience = investmentExperience;
    }

    public void changeInvestmentPreferredPeriod(InvestmentPreferredPeriod investmentPreferredPeriod) {
        this.investmentPreferredPeriod = investmentPreferredPeriod;
    }

    public void changeRiskTakingAbility(RiskTakingAbility riskTakingAbility) {
        this.riskTakingAbility = riskTakingAbility;
    }
}
