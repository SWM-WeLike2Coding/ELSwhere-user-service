package com.wl2c.elswhereuserservice.domain.user.controller;

import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestCreateInvestmentPropensityDto;
import com.wl2c.elswhereuserservice.domain.user.model.dto.response.ResponseInvestmentPropensityDto;
import com.wl2c.elswhereuserservice.domain.user.service.UserInvestmentPropensityService;
import com.wl2c.elswhereuserservice.global.model.dto.ResponseIdDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static java.lang.Long.parseLong;

@Tag(name = "사용자의 투자 성향 설문조사", description = "사용자의 투자 성향 설문조사 관련 api")
@RestController
@RequestMapping("/v1/propensity/survey")
@RequiredArgsConstructor
public class UserInvestmentPropensityController {

    private final UserInvestmentPropensityService userInvestmentPropensityService;

    /**
     * 설문 조사 내용 등록
     * <p>
     *      <br/>
     *      설문 조사 첫 등록 및 재참여도 해당됩니다.<br/>
     *      설문 조사 양식은 아래 타입에 맞춰서 기입해주세요.
     *
     *      investmentExperience(ELS 상품 투자 경험) : YES(있음), NO(없음)
     *      investmentPreferredPeriod(투자 선호 기간) : LESS_THAN_A_YEAR(1년 미만), A_YEAR_OR_TWO(1~2년), MORE_THAN_THREE_YEARS(3년 이상)
     *      riskTakingAbility(투자자 위험 감수 능력) : RISK_TAKING_TYPE(위험 감수형), STABILITY_SEEKING_TYPE(안정 추구형)
     * </p>
     *
     * @param dto 설문 조사 양식 dto
     */
    @PostMapping
    public void create(HttpServletRequest request,
                                @Valid @RequestBody RequestCreateInvestmentPropensityDto dto) {
        userInvestmentPropensityService.create(parseLong(request.getHeader("requestId")), dto);
    }

    /**
     * 설문 조사 내용 조회
     *<p>
     *      설문 조사에 참여한 적이 없는 경우, "notfound.survey-participation" 오류가 발생합니다.
     *</p>
     *
     * @return 사용자가 작성한 설문 조사 내용 dto
     */
    @GetMapping
    public ResponseInvestmentPropensityDto read(HttpServletRequest request) {
        return userInvestmentPropensityService.read(parseLong(request.getHeader("requestId")));
    }

    /**
     * 설문 조사 참여 여부 확인
     * <p>
     *     설문 조사에 참여한 경우, "ok"<br/>
     *     설문 조사에 참여한 적이 없는 경우, "notfound.survey-participation" 오류가 발생합니다.
     * </p>
     */
    @GetMapping("/check")
    public void checkSurveyParticipation(HttpServletRequest request) {
        userInvestmentPropensityService.checkSurveyParticipation(parseLong(request.getHeader("requestId")));
    }

}
