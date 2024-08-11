package com.wl2c.elswhereuserservice.domain.user.repository;


import com.wl2c.elswhereuserservice.domain.user.model.UserInfo;

import java.time.Instant;
import java.util.Optional;

public interface UserInfoMemoryRepository {

    Optional<UserInfo> getUserInfo(Long userId, Instant now);

    void setUserInfo(Long userId, UserInfo userInfo, Instant now);

    void removeUserInfo(Long userId);
}
