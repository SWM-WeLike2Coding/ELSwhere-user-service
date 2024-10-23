package com.wl2c.elswhereuserservice.domain.user.service;

import com.wl2c.elswhereuserservice.domain.user.exception.NotOAuthAuthorizedException;
import com.wl2c.elswhereuserservice.domain.user.exception.RequiredTermsAgreementException;
import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestIsAgreedToTermsDto;
import com.wl2c.elswhereuserservice.domain.user.model.dto.response.ResponseLoginDto;
import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import com.wl2c.elswhereuserservice.domain.user.repository.SignupAuthRepository;
import com.wl2c.elswhereuserservice.domain.user.repository.UserRepository;
import com.wl2c.elswhereuserservice.global.auth.jwt.AuthenticationToken;
import com.wl2c.elswhereuserservice.global.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignupService {

    public static final String OAUTH_AUTH_NAME = "oauth";

    private final Clock clock;
    private final SignupAuthRepository signupAuthRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    private final UserInfoService userInfoService;

    @Transactional
    public ResponseLoginDto signup(String signupToken, RequestIsAgreedToTermsDto requestIsAgreedToTermsDto) {
        User user = getUserInfo(signupToken);

        boolean deleted = deleteUserInfo(signupToken);
        if (!deleted)
            log.error("Can't delete user signup authentication: oauth user info");

        if (!requestIsAgreedToTermsDto.isAgreed()) {
            throw new RequiredTermsAgreementException();
        }

        userRepository.save(user);

        AuthenticationToken token = jwtProvider.issue(user);
        userInfoService.cacheUserInfo(user.getId(), user);

        return new ResponseLoginDto(token);
    }

    private User getUserInfo(String signupToken) {
        Instant now = Instant.now(clock);
        return signupAuthRepository.getAuthPayload(signupToken, OAUTH_AUTH_NAME, User.class, now)
                .orElseThrow(NotOAuthAuthorizedException::new);
    }

    private boolean deleteUserInfo(String signupToken) {
        return signupAuthRepository.deleteAuthPayload(signupToken, OAUTH_AUTH_NAME);
    }

}
