package com.wl2c.elswhereuserservice.domain.user.service;

import com.wl2c.elswhereuserservice.domain.user.exception.UserNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestCreateHoldingDto;
import com.wl2c.elswhereuserservice.domain.user.model.entity.Holding;
import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import com.wl2c.elswhereuserservice.domain.user.repository.UserHoldingRepository;
import com.wl2c.elswhereuserservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserHoldingService {

    private final UserRepository userRepository;
    private final UserHoldingRepository userHoldingRepository;

    @Transactional
    public void create(Long userId, RequestCreateHoldingDto requestCreateHoldingDto) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // TODO: product_id가 존재하는지 검증이 필요할 것 같음

        Holding holdings = Holding.builder()
                .user(user)
                .productId(requestCreateHoldingDto.getProductId())
                .price(requestCreateHoldingDto.getPrice())
                .build();
        userHoldingRepository.save(holdings);
    }
}
