package com.wl2c.elswhereuserservice.domain.user.service;

import com.wl2c.elswhereuserservice.domain.user.exception.UserNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import com.wl2c.elswhereuserservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.wl2c.elswhereuserservice.domain.user.model.UserStatus.INACTIVE;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserWithdrawService {

    private final UserRepository userRepository;
    private final UserInfoService cacheService;

    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.changeStatus(INACTIVE);
        cacheService.invalidateUserInfo(userId);
    }

}
