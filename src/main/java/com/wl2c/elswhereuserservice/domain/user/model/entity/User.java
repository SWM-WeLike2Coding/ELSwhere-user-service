package com.wl2c.elswhereuserservice.domain.user.model.entity;

import com.wl2c.elswhereuserservice.domain.user.service.UserInfoService;
import com.wl2c.elswhereuserservice.domain.user.model.SocialType;
import com.wl2c.elswhereuserservice.domain.user.model.UserStatus;
import com.wl2c.elswhereuserservice.global.auth.role.UserRole;
import com.wl2c.elswhereuserservice.global.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    public static String DELETED_USER = "탈퇴한 회원";

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Holding> holdingList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interest> interestList = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private InvestmentPropensity investmentPropensity;

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

    public String getName() {
        if (!getUserStatus().isActive()) {
            return DELETED_USER;
        }
        return this.name;
    }

    public String getNickname() {
        if (!getUserStatus().isActive()) {
            return DELETED_USER;
        }
        return this.nickname;
    }

    /**
     * 닉네임을 변경합니다.
     *
     * @param nickname 닉네임
     */
    public void changeNickName(String nickname) {
        this.nickname = nickname;
    }

    /**
     * User 상태를 변경합니다.
     * User정보 캐시를 삭제하기위해 {@link UserInfoService}.invalidateUserInfo를 호출해야 합니다.
     *
     * @param userStatus 상태
     */
    public void changeStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}
