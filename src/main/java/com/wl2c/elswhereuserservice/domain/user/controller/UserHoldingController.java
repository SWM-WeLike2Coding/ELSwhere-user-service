package com.wl2c.elswhereuserservice.domain.user.controller;

import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestCreateHoldingDto;
import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestUpdateHoldingDto;
import com.wl2c.elswhereuserservice.domain.user.service.UserHoldingService;
import com.wl2c.elswhereuserservice.global.model.dto.ResponseIdDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import static java.lang.Long.parseLong;

@Tag(name = "사용자의 보유중인 상품", description = "사용자의 보유중인 상품 관련 api")
@RestController
@RequestMapping("/v1/holding")
@RequiredArgsConstructor
public class UserHoldingController {

    private final UserHoldingService userHoldingService;

    /**
     * 보유 상품 등록
     * @param dto 상품 등록을 위한 정보(상품 id, 금액)
     * @return 보유 상품 id
     */
    @PostMapping
    public ResponseIdDto create(HttpServletRequest request,
                                @Valid @RequestBody RequestCreateHoldingDto dto) {
        return userHoldingService.create(parseLong(request.getHeader("requestId")), dto);
    }

    /**
     * 특정 보유 상품 금액 수정
     * @param dto 보유 상품 수정을 위한 정보(상품 id, 금액)
     */
    @PatchMapping
    public void update(HttpServletRequest request,
                       @Valid @RequestBody RequestUpdateHoldingDto dto) {
        userHoldingService.update(parseLong(request.getHeader("requestId")), dto);
    }

    /**
     * 특정 보유 상품 삭제
     */
    @DeleteMapping
    public void update(HttpServletRequest request,
                       @RequestParam Long holdingId) {
        userHoldingService.delete(parseLong(request.getHeader("requestId")), holdingId);
    }
}
