package com.wl2c.elswhereuserservice.domain.user.service;

import com.wl2c.elswhereuserservice.client.analysis.api.AnalysisServiceClient;
import com.wl2c.elswhereuserservice.client.analysis.dto.response.ResponsePriceRatioDto;
import com.wl2c.elswhereuserservice.client.product.api.ProductServiceClient;
import com.wl2c.elswhereuserservice.client.product.dto.request.RequestProductIdListDto;
import com.wl2c.elswhereuserservice.client.product.dto.response.ResponseSingleProductDto;
import com.wl2c.elswhereuserservice.client.product.dto.response.ResponseSummarizedProductForHoldingDto;
import com.wl2c.elswhereuserservice.client.product.exception.ProductNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.exception.AlreadyHoldingException;
import com.wl2c.elswhereuserservice.domain.user.exception.HoldingNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.exception.UserNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.model.dto.list.SummarizedUserHoldingDto;
import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestCreateHoldingDto;
import com.wl2c.elswhereuserservice.domain.user.model.entity.Holding;
import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import com.wl2c.elswhereuserservice.domain.user.repository.UserHoldingRepository;
import com.wl2c.elswhereuserservice.domain.user.repository.UserRepository;
import com.wl2c.elswhereuserservice.global.model.dto.ResponseIdDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserHoldingService {

    private final CircuitBreakerFactory circuitBreakerFactory;

    private final UserRepository userRepository;
    private final UserHoldingRepository userHoldingRepository;

    private final ProductServiceClient productServiceClient;
    private final AnalysisServiceClient analysisServiceClient;

    @Transactional
    public ResponseIdDto create(Long userId, RequestCreateHoldingDto requestCreateHoldingDto) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("holdingCreateCircuitBreaker");
        circuitBreaker.run(() -> productServiceClient.getProduct(requestCreateHoldingDto.getProductId()),
                throwable -> new ProductNotFoundException());

        if (userHoldingRepository.findByUserIdAndProductId(userId, requestCreateHoldingDto.getProductId()).isPresent()) {
            throw new AlreadyHoldingException();
        } else {
            Holding holdings = Holding.builder()
                    .user(user)
                    .productId(requestCreateHoldingDto.getProductId())
                    .price(requestCreateHoldingDto.getPrice())
                    .build();
            userHoldingRepository.save(holdings);

            return new ResponseIdDto(holdings.getId());
        }
    }

    @Transactional
    public void update(Long userId, Long holdingId, BigDecimal price) {
        Holding holding = userHoldingRepository.findById(holdingId).orElseThrow(HoldingNotFoundException::new);

        if (holding.getUser().getId().equals(userId)) {
            userHoldingRepository.updateHolding(userId,
                    holdingId,
                    price);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Transactional
    public void delete(Long userId, Long holdingId) {
        Holding holding = userHoldingRepository.findById(holdingId).orElseThrow(HoldingNotFoundException::new);

        if (holding.getUser().getId().equals(userId)) {
            userHoldingRepository.deleteHolding(userId, holdingId);
        } else {
            throw new UserNotFoundException();
        }
    }

    public List<SummarizedUserHoldingDto> read(Long userId) {
        List<Holding> holdingList = userHoldingRepository.findAllByUserId(userId);
        if (holdingList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> productIdList = holdingList.stream()
                .map(Holding::getProductId)
                .toList();

        log.info("Before call the product microservice");
        CircuitBreaker circuitBreakerAboutProduct = circuitBreakerFactory.create("holdingReadCircuitBreakerAboutProduct");
        List<ResponseSummarizedProductForHoldingDto> responseSummarizedProductForHoldingDtoList =
                circuitBreakerAboutProduct.run(() -> productServiceClient.holdingListByProductIds(new RequestProductIdListDto(productIdList)),
                        throwable -> new ArrayList<>());
        log.info("after called the product microservice");

        log.info("Before call the analysis microservice");
        CircuitBreaker circuitBreakerAboutAnalysis = circuitBreakerFactory.create("holdingReadCircuitBreakerAboutAnalysis");
        List<ResponsePriceRatioDto> responsePriceRatioDtoList =
                circuitBreakerAboutAnalysis.run(() -> analysisServiceClient.getPriceRatioList(new RequestProductIdListDto(productIdList)),
                        throwable -> new ArrayList<>());
        log.info("after called the analysis microservice");

        // 상품 ID를 키로 하는 Map 생성
        Map<Long, ResponseSummarizedProductForHoldingDto> productMap = responseSummarizedProductForHoldingDtoList.stream()
                .collect(Collectors.toMap(ResponseSummarizedProductForHoldingDto::getId, dto -> dto));

        // 상품 ID를 키로 하는 Map 생성
        Map<Long, ResponsePriceRatioDto> priceRatioMap = responsePriceRatioDtoList.stream()
                .collect(Collectors.toMap(ResponsePriceRatioDto::getId, dto -> dto));

        return holdingList.stream()
                .map(holding -> {
                    Long productId = holding.getProductId();
                    ResponseSummarizedProductForHoldingDto productDto = productMap.get(productId);
                    ResponsePriceRatioDto priceRatioDto = priceRatioMap.get(productId);

                    if (productDto == null) {
                        return null;
                    } else if (priceRatioDto == null) {
                        return new SummarizedUserHoldingDto(holding, productDto, null);
                    }

                    return new SummarizedUserHoldingDto(holding, productDto, priceRatioDto);

                })
                .filter(Objects::nonNull)  // null 값 필터링
                .toList();
    }

    // TODO: 전체 보유 상품 금액 합계(현재 보유중인 상품과 상환 완료한 상품 금액 구분해서 보여주는 리스트)

    // TODO: 특정 보유 상품에 대한 평가 금액 반영 및 낙인 도달 여부 확인

    // TODO: 현재 보유중인 상품 리스트

    // TODO: 상환 완료한 상품 금액 구분해서 보여주는 리스트
}
