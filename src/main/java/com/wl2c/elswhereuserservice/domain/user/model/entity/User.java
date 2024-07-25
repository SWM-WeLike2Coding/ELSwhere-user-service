package com.wl2c.elswhereuserservice.domain.user.model.entity;

import com.wl2c.elswhereuserservice.domain.user.model.SocialType;
import com.wl2c.elswhereuserservice.domain.user.model.UserStatus;
import com.wl2c.elswhereuserservice.global.auth.role.UserRole;
import com.wl2c.elswhereuserservice.global.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @NotNull
    private String socialId;

    @NotNull
    @Enumerated(STRING)
    private SocialType socialType;

    @NotNull
    private String email;

    @NotNull
    @Column(length = 20)
    private String name;

    @NotNull
    @Column(length = 20)
    private String nickname;

    @Enumerated(STRING)
    private UserStatus userStatus;

    @Enumerated(STRING)
    private UserRole userRole;

    @Builder
    private User (@NonNull String socialId,
                  @NonNull SocialType socialType,
                  @NonNull String email,
                  @NonNull String name,
                  @NonNull String nickname,
                  UserStatus userStatus,
                  UserRole userRole) {
        this.socialId = socialId;
        this.socialType = socialType;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.userStatus = userStatus;
        this.userRole = userRole;
    }

    /**
     * 닉네임을 변경합니다.
     *
     * @param nickname 닉네임
     */
    public void changeNickName(String nickname) {
        this.nickname = nickname;
    }
}
