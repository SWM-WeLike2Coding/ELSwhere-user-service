package com.wl2c.elswhereuserservice.domain.user.service;

import com.wl2c.elswhereuserservice.domain.user.exception.HoldingNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.exception.UserNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestCreateHoldingDto;
import com.wl2c.elswhereuserservice.domain.user.model.entity.Holding;
import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import com.wl2c.elswhereuserservice.domain.user.repository.UserHoldingRepository;
import com.wl2c.elswhereuserservice.domain.user.repository.UserRepository;
import com.wl2c.elswhereuserservice.global.model.dto.ResponseIdDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserHoldingService {

    private final UserRepository userRepository;
    private final UserHoldingRepository userHoldingRepository;

    @Transactional
    public ResponseIdDto create(Long userId, RequestCreateHoldingDto requestCreateHoldingDto) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // TODO: product_id가 존재하는지 검증이 필요할 것 같음

        Holding holdings = Holding.builder()
                .user(user)
                .productId(requestCreateHoldingDto.getProductId())
                .price(requestCreateHoldingDto.getPrice())
                .build();
        userHoldingRepository.save(holdings);

        return new ResponseIdDto(holdings.getId());
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

    // TODO: 전체 보유 상품 금액 합계(현재 보유중인 상품과 상환 완료한 상품 금액 구분해서 보여주는 리스트)

    // TODO: 특정 보유 상품에 대한 평가 금액 반영 및 낙인 도달 여부 확인

    // TODO: 현재 보유중인 상품 리스트

    // TODO: 상환 완료한 상품 금액 구분해서 보여주는 리스트
}
