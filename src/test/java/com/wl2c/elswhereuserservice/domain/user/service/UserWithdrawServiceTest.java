package com.wl2c.elswhereuserservice.domain.user.service;

import com.wl2c.elswhereuserservice.domain.user.model.SocialType;
import com.wl2c.elswhereuserservice.domain.user.model.UserStatus;
import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import com.wl2c.elswhereuserservice.domain.user.repository.UserRepository;
import com.wl2c.elswhereuserservice.mock.UserMock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserWithdrawServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserInfoService cacheService;

    @InjectMocks
    private UserWithdrawService userWithdrawService;

    @Test
    @DisplayName("회원 탈퇴 - INACTIVE로 상태 변경이 잘 되는지 확인")
    void withdraw() {
        // given
        User user = UserMock.create(SocialType.GOOGLE, "1234@gmail.com");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // when
        userWithdrawService.withdraw(user.getId());

        // then
        assertThat(user.getUserStatus()).isEqualTo(UserStatus.INACTIVE);
        verify(cacheService).invalidateUserInfo(user.getId());
    }
}