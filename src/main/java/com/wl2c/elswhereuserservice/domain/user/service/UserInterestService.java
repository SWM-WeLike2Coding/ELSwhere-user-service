package com.wl2c.elswhereuserservice.domain.user.service;

import com.wl2c.elswhereuserservice.client.product.api.ProductServiceClient;
import com.wl2c.elswhereuserservice.client.product.dto.request.RequestProductIdListDto;
import com.wl2c.elswhereuserservice.client.product.dto.response.ResponseSummarizedProductDto;
import com.wl2c.elswhereuserservice.client.product.exception.ProductNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.exception.AlreadyInterestException;
import com.wl2c.elswhereuserservice.domain.user.exception.InterestNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.exception.UserNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestCreateInterestDto;
import com.wl2c.elswhereuserservice.domain.user.model.dto.list.SummarizedUserInterestDto;
import com.wl2c.elswhereuserservice.domain.user.model.entity.Interest;
import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import com.wl2c.elswhereuserservice.domain.user.repository.UserInterestRepository;
import com.wl2c.elswhereuserservice.domain.user.repository.UserRepository;
import com.wl2c.elswhereuserservice.global.model.dto.ResponseIdDto;
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
public class UserInterestService {

    private final CircuitBreakerFactory circuitBreakerFactory;

    private final UserRepository userRepository;
    private final UserInterestRepository userInterestRepository;

    private final ProductServiceClient productServiceClient;

    @Transactional
    public ResponseIdDto create(Long userId, RequestCreateInterestDto requestCreateInterestDto) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        log.info("Before call the product microservice");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("interestCreateCircuitBreaker");
        circuitBreaker.run(() -> productServiceClient.getProduct(requestCreateInterestDto.getProductId()),
                throwable -> new ProductNotFoundException());
        log.info("After called the product microservice");

        if (userInterestRepository.findByUserIdAndProductId(userId, requestCreateInterestDto.getProductId()).isPresent()) {
            throw new AlreadyInterestException();
        } else {
            Interest interest = Interest.builder()
                    .user(user)
                    .productId(requestCreateInterestDto.getProductId())
                    .build();
            userInterestRepository.save(interest);

            return new ResponseIdDto(interest.getId());
        }
    }

    public List<SummarizedUserInterestDto> read(Long userId) {
        List<Interest> interestList = userInterestRepository.findAllByUserId(userId);
        if (interestList.isEmpty()) {
            throw new InterestNotFoundException();
        }

        List<Long> productIdList = interestList.stream()
                .map(Interest::getProductId)
                .toList();

        log.info("Before call the product microservice");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("interestReadCircuitBreaker");
        List<ResponseSummarizedProductDto> responseSummarizedProductDtoList =
                circuitBreaker.run(() -> productServiceClient.listByProductIds(new RequestProductIdListDto(productIdList)),
                throwable -> new ArrayList<>());
        log.info("fter called the product microservice");

        List<SummarizedUserInterestDto> result = new ArrayList<>();
        for (Interest interest : interestList) {
            for (ResponseSummarizedProductDto responseSummarizedProductDto : responseSummarizedProductDtoList) {
                if (interest.getProductId().equals(responseSummarizedProductDto.getId())) {
                    result.add(new SummarizedUserInterestDto(interest.getId(), responseSummarizedProductDto));
                    break;
                }
            }
        }

        return result;
    }

    @Transactional
    public void delete(Long userId, Long interestId) {
        Interest interest = userInterestRepository.findById(interestId).orElseThrow(InterestNotFoundException::new);

        if (interest.getUser().getId().equals(userId)) {
            userInterestRepository.deleteInterest(userId, interestId);
        } else {
            throw new UserNotFoundException();
        }
    }
}
