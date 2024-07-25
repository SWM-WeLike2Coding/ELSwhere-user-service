package com.wl2c.elswhereuserservice.domain.user.model;

import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class UserInfo {
    private final Long userId;
    private final SocialType socialType;
    private final String name;
    private final String nickname;
    private final UserStatus userStatus;

    public UserInfo(User user) {
        this.userId = user.getId();
        this.socialType = user.getSocialType();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.userStatus = user.getUserStatus();
    }
}
