package com.wl2c.elswhereuserservice.domain.user.controller;

import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestNickNameChangeDto;
import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestRefreshTokenDto;
import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestIsAgreedToTermsDto;
import com.wl2c.elswhereuserservice.domain.user.model.dto.response.ResponseLoginDto;
import com.wl2c.elswhereuserservice.domain.user.model.dto.response.ResponseRefreshTokenDto;
import com.wl2c.elswhereuserservice.domain.user.model.dto.response.ResponseUserInfoDto;
import com.wl2c.elswhereuserservice.domain.user.model.dto.response.ResponseUserNicknameDto;
import com.wl2c.elswhereuserservice.domain.user.service.SignupService;
import com.wl2c.elswhereuserservice.domain.user.service.UserInfoService;
import com.wl2c.elswhereuserservice.domain.user.service.UserService;
import com.wl2c.elswhereuserservice.domain.user.service.UserWithdrawService;
import com.wl2c.elswhereuserservice.global.model.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private final SignupService signupService;

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
     * 사용자 id에 대한 닉네임 조회
     * <p>
     *     게시글 작성자 조회 등, 서비스 통신 간에 사용되는 api 입니다. (관리자 권한)
     * </p>
     *
     * @return 사용자 닉네임
     */
    @GetMapping("/nickname/{id}")
    public ResponseUserNicknameDto getUserNickname(HttpServletRequest request,
                                                   @PathVariable Long id) {
        return userInfoService.getUserNickname(request.getHeader("requestRole"), id);
    }

    /**
     * 닉네임 변경
     * <p>
     *     조건<br/>
     *     문자열은 영어 대문자, 소문자, 숫자, 한글 자모 및 음절, 언더스코어, 공백만 포함될 수 있습니다.<br/>
     *     문자열의 길이는 3자에서 16자 사이여야 합니다.<br/>
     *     문자열 내에 두 개 이상의 연속된 공백이 없어야 합니다.<br/>
     * </p>
     *
     * @param dto 요청 body
     */
    @PatchMapping("/change/nickname")
    public void changeNickName(HttpServletRequest request,
                               @Valid @RequestBody RequestNickNameChangeDto dto) {
        userService.changeNickname(parseLong(request.getHeader("requestId")), dto);
    }

    /**
     * 닉네임 중복 확인
     *
     * @param dto 요청 body
     */
    @PostMapping("/check/nickname")
    public void checkAlreadyNickname(@Valid @RequestBody RequestNickNameChangeDto dto) {
        userService.checkAlreadyNickname(dto.getNickname());
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
     * 회원가입
     * <p>
     *     각 Oauth를 통해 가입을 진행할 때,
     *     서비스 이용 약관 페이지로 리다이렉션을 하면서 서버에서 건내준 signup_token을 해당 api에서 이용합니다.
     * </p>
     *
     * @param dto           서비스 이용 약관 동의 여부 및 약관 버전에 대한 dto
     * @param signupToken   회원가입 토큰
     * @return              액세스 토큰 및 리프레쉬 토큰에 대한 dto
     */
    @PostMapping("/signup/{signup-token}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "액세스 토큰 및 리프레쉬 토큰에 대한 dto",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseLoginDto.class))),
            @ApiResponse(responseCode = "400", description = "OAuth 인증이 필요합니다.",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "서비스 이용 약관 동의가 필요합니다.",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseLoginDto signup(@Valid @RequestBody RequestIsAgreedToTermsDto dto,
                                   @PathVariable("signup-token") String signupToken) {
        return signupService.signup(signupToken, dto);
    }

    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {
        userService.logout(request);
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
