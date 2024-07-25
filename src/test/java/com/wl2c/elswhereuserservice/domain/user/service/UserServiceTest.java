package com.wl2c.elswhereuserservice.domain.user.service;

import com.wl2c.elswhereuserservice.domain.user.model.SocialType;
import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestNickNameChangeDto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserInfoService cacheService;

    @InjectMocks
    private UserService userService;


    @Test
    @DisplayName("닉네임 변경")
    void changeUserNickName() {
        // given
        User user = UserMock.create(SocialType.GOOGLE, "1234@gmail.com");
        RequestNickNameChangeDto dto = new RequestNickNameChangeDto("바꾸는 이름");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // when
        userService.changeNickname(user.getId(), dto);

        // then
        assertThat(user.getNickname()).isEqualTo(dto.getNickname());
        verify(cacheService).invalidateUserInfo(user.getId());
    }
}