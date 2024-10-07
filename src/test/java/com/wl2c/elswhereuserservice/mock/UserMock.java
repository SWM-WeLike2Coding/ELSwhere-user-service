package com.wl2c.elswhereuserservice.mock;

import com.wl2c.elswhereuserservice.domain.user.model.SocialType;
import com.wl2c.elswhereuserservice.domain.user.model.UserStatus;
import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import com.wl2c.elswhereuserservice.global.auth.role.UserRole;
import com.wl2c.elswhereuserservice.util.EntityUtil;
import com.wl2c.elswhereuserservice.util.RandomGen;

public class UserMock {

    public static final String SOCIAL_ID = "12345678";
    public static final String NAME = "username";
    public static final String NICKNAME = "nickname";
    public static final String EMAIL = "1234@gmail.com";
    public static final String SOCIAL_TYPE = String.valueOf(SocialType.GOOGLE);

    public static User createDummy() {
        return createDummy(RandomGen.nextLong());
    }

    public static User createDummy(Long userId) {
        return create(userId, NAME, SocialType.valueOf(SOCIAL_TYPE), EMAIL, UserRole.USER);
    }


    public static User create(SocialType socialType, String email) {
        return create(RandomGen.nextLong(), NAME, socialType, email, UserRole.USER);
    }

    public static User create(Long userId, String username, SocialType socialType, String email, UserRole role) {

        User user = User.builder()
                .socialId(SOCIAL_ID)
                .socialType(socialType)
                .email(email)
                .name(username)
                .nickname(NICKNAME)
                .userStatus(UserStatus.ACTIVE)
                .userRole(role)
                .build();

        EntityUtil.injectId(User.class, user, userId);
        return user;
    }
}
