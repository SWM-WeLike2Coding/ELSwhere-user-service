package com.wl2c.elswhereuserservice.mock;

import com.wl2c.elswhereuserservice.domain.user.model.SocialType;
import com.wl2c.elswhereuserservice.domain.user.model.UserInfo;
import com.wl2c.elswhereuserservice.domain.user.model.UserStatus;
import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import com.wl2c.elswhereuserservice.global.auth.role.UserRole;

public class UserInfoMock {

    public static UserInfo create() {
        return new UserInfo(UserMock.createDummy());
    }

    public static UserInfo create(String academicStatus) {
        User user = User.builder()
                .socialId("12345678")
                .socialType(SocialType.GOOGLE)
                .email("1234@gmail.com")
                .name("name")
                .nickname("nickname")
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.USER)
                .build();

        return new UserInfo(user);
    }
}
