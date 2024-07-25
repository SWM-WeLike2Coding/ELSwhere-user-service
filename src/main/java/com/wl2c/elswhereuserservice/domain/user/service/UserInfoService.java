package com.wl2c.elswhereuserservice.domain.user.service;

import com.wl2c.elswhereuserservice.domain.user.exception.UserNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.model.UserInfo;
import com.wl2c.elswhereuserservice.domain.user.model.dto.response.ResponseUserInfoDto;
import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import com.wl2c.elswhereuserservice.domain.user.repository.UserInfoMemoryRepository;
import com.wl2c.elswhereuserservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final Clock clock;
    private final UserRepository persistenceRepository;
    private final UserInfoMemoryRepository memoryRepository;
    @Transactional
    public ResponseUserInfoDto getFullUserInfo(Long userId) {
        User user = persistenceRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // TODO : 좋아요 수 등등 추가 정보 가져오기

        return new ResponseUserInfoDto(
                user.getSocialType(),
                user.getEmail(),
                user.getName(),
                user.getNickname(),
                user.getUserRole().isAdmin()
        );
    }

    public void cacheUserInfo(Long userId, User user) {
        UserInfo userInfo = new UserInfo(user);
        memoryRepository.setUserInfo(userId, userInfo, Instant.now(clock));
    }
}
