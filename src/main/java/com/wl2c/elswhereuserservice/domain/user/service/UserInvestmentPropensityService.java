package com.wl2c.elswhereuserservice.domain.user.service;

import com.wl2c.elswhereuserservice.client.analysis.api.AnalysisServiceClient;
import com.wl2c.elswhereuserservice.client.analysis.dto.request.RequestInvestmentPropensityInformationDto;
import com.wl2c.elswhereuserservice.client.product.api.ProductServiceClient;
import com.wl2c.elswhereuserservice.client.product.dto.list.SummarizedProductDto;
import com.wl2c.elswhereuserservice.domain.user.exception.UserNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.exception.SurveyNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestCreateInvestmentPropensityDto;
import com.wl2c.elswhereuserservice.domain.user.model.dto.response.ResponseInvestmentPropensityDto;
import com.wl2c.elswhereuserservice.domain.user.model.entity.InvestmentPropensity;
import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import com.wl2c.elswhereuserservice.domain.user.repository.UserRepository;
import com.wl2c.elswhereuserservice.domain.user.repository.UserInvestmentPropensityRepository;
import com.wl2c.elswhereuserservice.global.model.dto.ResponsePage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserInvestmentPropensityService {

    private final CircuitBreakerFactory circuitBreakerFactory;

    private final UserInvestmentPropensityRepository userInvestmentPropensityRepository;
    private final UserRepository userRepository;

    private final ProductServiceClient productServiceClient;
    private final AnalysisServiceClient analysisServiceClient;

    @Transactional
    public void create(Long userId, RequestCreateInvestmentPropensityDto dto) {
        if (userInvestmentPropensityRepository.findByUserId(userId).isPresent()) {
            InvestmentPropensity investmentPropensity = userInvestmentPropensityRepository.findByUserId(userId).get();

            investmentPropensity.changeInvestmentExperience(dto.getInvestmentExperience());
            investmentPropensity.changeRiskPropensity(dto.getRiskPropensity());
            investmentPropensity.changeRepaymentOption(dto.getRepaymentOption());
            investmentPropensity.changeMinPreferredReturn(dto.getMinPreferredReturn());

        } else {
            User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

            InvestmentPropensity investmentPropensity = InvestmentPropensity.builder()
                    .user(user)
                    .investmentExperience(dto.getInvestmentExperience())
                    .riskPropensity(dto.getRiskPropensity())
                    .repaymentOption(dto.getRepaymentOption())
                    .minPreferredReturn(dto.getMinPreferredReturn())
                    .build();
            userInvestmentPropensityRepository.save(investmentPropensity);

        }
    }

    public ResponseInvestmentPropensityDto read(Long userId) {
        InvestmentPropensity investmentPropensity = userInvestmentPropensityRepository.findByUserId(userId).orElseThrow(SurveyNotFoundException::new);
        return new ResponseInvestmentPropensityDto(investmentPropensity);
    }

    public void checkSurveyParticipation(Long userId) {
        userInvestmentPropensityRepository.findByUserId(userId).orElseThrow(SurveyNotFoundException::new);
    }

    public ResponsePage<SummarizedProductDto> personalizedProducts(Long userId,
                                                                   String type,
                                                                   Pageable pageable) {

        InvestmentPropensity investmentPropensity = userInvestmentPropensityRepository.findByUserId(userId).orElseThrow(SurveyNotFoundException::new);

        // 청약 중인 상품 가져오기 (product-service)
        CircuitBreaker onSaleProductsCircuitBreaker = circuitBreakerFactory.create("onSaleProductsCircuitBreaker");
        ResponsePage<SummarizedProductDto> onSaleProducts = onSaleProductsCircuitBreaker.run(() -> productServiceClient.listByOnSale(type, pageable),
                throwable -> ResponsePage.empty(pageable));

        // 조회한 상품 중, 사용자의 최소 선호 수익률 조건에 맞는 상품만 필터링
        List<SummarizedProductDto> filteredMinReturnProducts = onSaleProducts.getContent().stream()
                .filter(dto -> dto.getYieldIfConditionsMet().compareTo(investmentPropensity.getMinPreferredReturn()) >= 0)
                .toList();
        if (filteredMinReturnProducts.isEmpty()) {
            return ResponsePage.empty(pageable);
        }

        // 최소 수익률이 필터링된 상품들 중, 위험 감수 능력과 회망상환기간에 부합하는 상품 id 리스트 가져오기 (analysis-service)
        List<Long> filteredMinReturnProductIds = filteredMinReturnProducts.stream()
                .map(SummarizedProductDto::getId)
                .toList();

        RequestInvestmentPropensityInformationDto requestInvestmentPropensityInformationDto = new RequestInvestmentPropensityInformationDto(
                filteredMinReturnProductIds,
                investmentPropensity.getRiskPropensity(),
                investmentPropensity.getRepaymentOption()
        );
        CircuitBreaker satisfiedInvestmentPropensityCircuitBreaker = circuitBreakerFactory.create("satisfiedInvestmentPropensityCircuitBreaker");
        List<Long> satisfiedInvestmentPropensityProductIds = satisfiedInvestmentPropensityCircuitBreaker.run(() ->
                analysisServiceClient.getSatisfiedInvestmentPropensityProducts(requestInvestmentPropensityInformationDto),
                throwable -> new ArrayList<>());

        // 필터링된 상품 중 만족하는 상품 ID가 있는 상품만 추출
        List<SummarizedProductDto> finalFilteredProducts = filteredMinReturnProducts.stream()
                .filter(dto -> satisfiedInvestmentPropensityProductIds.contains(dto.getId()))
                .toList();

        // 필터링된 리스트를 기반으로 ResponsePage 생성
        Page<SummarizedProductDto> page = new PageImpl<>(finalFilteredProducts, pageable, finalFilteredProducts.size());
        return new ResponsePage<>(page);

    }

}
