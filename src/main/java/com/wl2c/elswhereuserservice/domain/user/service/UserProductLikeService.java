package com.wl2c.elswhereuserservice.domain.user.service;

import com.wl2c.elswhereuserservice.client.product.api.ProductServiceClient;
import com.wl2c.elswhereuserservice.client.product.dto.request.RequestProductIdListDto;
import com.wl2c.elswhereuserservice.client.product.dto.list.SummarizedProductDto;
import com.wl2c.elswhereuserservice.domain.user.exception.ProductLikeNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.model.entity.ProductLike;
import com.wl2c.elswhereuserservice.domain.user.repository.UserProductLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserProductLikeService {

    private final CircuitBreakerFactory circuitBreakerFactory;
    private final ProductServiceClient productServiceClient;

    private final UserProductLikeRepository userProductLikeRepository;

    public void findByProductIdAndUserId(Long productId, Long userId) {
        userProductLikeRepository.findByProductIdAndUserId(productId, userId).orElseThrow(ProductLikeNotFoundException::new);
    }

    public List<SummarizedProductDto> findLikedProducts(Long userId) {

        // redis -> db로 dump 요청
        productServiceClient.dumpLike(String.valueOf(userId));

        List<ProductLike> productLikeList = userProductLikeRepository.findAllByUserId(userId);
        List<Long> productIdList = productLikeList.stream()
                .map(ProductLike::getProductId)
                .toList();

        log.info("Before call the product microservice");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("likedProductReadCircuitBreaker");
        List<SummarizedProductDto> summarizedProductDtoList =
                circuitBreaker.run(() -> productServiceClient.listByProductIds(new RequestProductIdListDto(productIdList)),
                        throwable -> new ArrayList<>());
        log.info("after called the product microservice");

        return summarizedProductDtoList;

    }
}
