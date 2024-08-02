package com.wl2c.elswhereuserservice.domain.user.controller;

import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestNickNameChangeDto;
import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestRefreshTokenDto;
import com.wl2c.elswhereuserservice.domain.user.model.dto.response.ResponseRefreshTokenDto;
import com.wl2c.elswhereuserservice.domain.user.model.dto.response.ResponseUserInfoDto;
import com.wl2c.elswhereuserservice.domain.user.service.UserInfoService;
import com.wl2c.elswhereuserservice.domain.user.service.UserService;
import com.wl2c.elswhereuserservice.domain.user.service.UserWithdrawService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static java.lang.Long.parseLong;

@Tag(name = "사용자", description = "사용자 인증 및 정보 관련 api")
@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserInfoService userInfoService;
    private final UserWithdrawService userWithdrawService;

    /**
     * 내 정보 조회
     *
     * @return 내 정보
     */
    @GetMapping
    public ResponseUserInfoDto getMyInfo(HttpServletRequest request) {
        return userInfoService.getFullUserInfo(parseLong(request.getHeader("requestId")));
    }

    /**
     * 닉네임 변경
     *
     * @param dto 요청 body
     */
    @PatchMapping("/change/nickname")
    public void changeNickName(HttpServletRequest request,
                               @Valid @RequestBody RequestNickNameChangeDto dto) {
        userService.changeNickname(parseLong(request.getHeader("requestId")), dto);
    }

    /**
     * 토큰 재발급
     *
     * @param dto 요청 body
     * @return 재발급된 로그인 인증 정보
     */
    @PostMapping("/reissue")
    public ResponseRefreshTokenDto refreshToken(HttpServletRequest request,
                                                @Valid @RequestBody RequestRefreshTokenDto dto) {
        return userService.refreshToken(request, dto.getRefreshToken());
    }

    /**
     * 회원탈퇴
     *
     * <p>회원은 바로 삭제되지 않고, 7일 뒤에 서버 배치작업에 의해 자동 삭제됩니다.</p>
     * <p>회원 관련 조회시에는 '탈퇴한 회원'이라고 조회됩니다.</p>
     */
    @DeleteMapping
    public void withdraw(HttpServletRequest request) {
        userWithdrawService.withdraw(parseLong(request.getHeader("requestId")));
    }
}
