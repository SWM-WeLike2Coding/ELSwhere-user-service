package com.wl2c.elswhereuserservice.domain.user.service;

import com.wl2c.elswhereuserservice.domain.user.model.SocialType;
import com.wl2c.elswhereuserservice.domain.user.model.dto.response.ResponseUserInfoDto;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserInfoService userInfoService;

    @Test
    @DisplayName("Full 내 정보 가져오기")
    void getFullUserInfo() {
        // given
        User user = UserMock.create(SocialType.GOOGLE, "1234@gmail.com");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // when
        ResponseUserInfoDto info = userInfoService.getFullUserInfo(user.getId());

        // then
        assertThat(info.getSocialType()).isEqualTo(SocialType.GOOGLE);
        assertThat(info.getEmail()).isEqualTo(user.getEmail());
        assertThat(info.getName()).isEqualTo(user.getName());
        assertThat(info.getNickname()).isEqualTo(user.getNickname());
        assertThat(info.isAdmin()).isEqualTo(user.getUserRole().isAdmin());
    }

}