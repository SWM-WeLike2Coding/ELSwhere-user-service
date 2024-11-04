package com.wl2c.elswhereuserservice.client.analysis.dto.request;

import com.wl2c.elswhereuserservice.domain.user.model.RepaymentOption;
import com.wl2c.elswhereuserservice.domain.user.model.RiskPropensity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RequestInvestmentPropensityInformationDto {

    @Schema(description = "상품 id 리스트", example = "[3, 6, 9, 12]")
    private final List<Long> productIdList;

    @Schema(description = "투자자 위험 감수 능력", example = "MEDIUM_RISK")
    private final RiskPropensity riskPropensity;

    @Schema(description = "희망 상환 기간", example = "EARLY_REPAYMENT")
    private final RepaymentOption repaymentOption;

}
