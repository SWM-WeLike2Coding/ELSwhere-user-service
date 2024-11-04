package com.wl2c.elswhereuserservice.domain.user.controller;

import com.wl2c.elswhereuserservice.client.product.dto.list.SummarizedProductDto;
import com.wl2c.elswhereuserservice.domain.user.service.UserInvestmentPropensityService;
import com.wl2c.elswhereuserservice.global.model.dto.ResponsePage;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.Long.parseLong;

@Tag(name = "사용자의 투자 성향에 맞는 청약 중인 상품", description = "사용자의 투자 성향에 맞는 청약 중인 상품 관련 api")
@RestController
@RequestMapping("/v1/propensity")
@RequiredArgsConstructor
public class UserInvestmentPropensityController {

    private final UserInvestmentPropensityService userInvestmentPropensityService;

    /**
     * 사용자의 투자 성향에 맞는 청약 중인 상품 리스트 조회
     * <p>
     *     해당하는 정렬 타입(type)에 맞게 문자열을 기입해주세요.
     *
     *     최신순 : latest
     *     낙인순 : knock-in
     *     수익률순 : profit
     *     청약 마감일순 : deadline
     * </p>
     * <p>
     *     <br/>
     *     스텝다운 유형의 상품에 대해서 AI가 분석한 각 상품의 safetyScore를 제공합니다. <br/>
     *     스텝다운 유형이 아니거나 스텝다운 유형이지만 분석 정보가 없는 경우에는 null 값으로 제공됩니다. <br/>
     * </p>
     *
     * @param type 정렬 타입
     * @return 페이징된 사용자의 투자 성향에 맞는 청약 중인 상품 목록
     */
    @GetMapping
    public ResponsePage<SummarizedProductDto> personalizedProducts(HttpServletRequest request,
                                                                   @RequestParam(name = "type") String type,
                                                                   @ParameterObject Pageable pageable) {
        return userInvestmentPropensityService.personalizedProducts(parseLong(request.getHeader("requestId")), type, pageable);
    }
}
