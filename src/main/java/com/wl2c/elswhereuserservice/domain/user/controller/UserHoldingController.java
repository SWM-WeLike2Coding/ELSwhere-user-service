package com.wl2c.elswhereuserservice.domain.user.controller;

import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestCreateHoldingDto;
import com.wl2c.elswhereuserservice.domain.user.service.UserHoldingService;
import com.wl2c.elswhereuserservice.global.model.dto.ResponseIdDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static java.lang.Long.parseLong;

@Tag(name = "사용자의 보유중인 상품", description = "사용자의 보유중인 상품 관련 api")
@RestController
@RequestMapping("/v1/holding")
@RequiredArgsConstructor
public class UserHoldingController {

    private final UserHoldingService userHoldingService;

    /**
     * 보유 상품 등록
     * <p>
     *     <br/>
     *     등록 시, 사용자의 보유 상품 id를 반환하게 됩니다.
     *     해당 보유 상품 id로 조회, 수정 및 삭제가 가능합니다.
     * </p>
     *
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
     *
     * @param id 수정할 보유 상품 id
     * @param price 수정할 금액
     */
    @PatchMapping("/{id}")
    public void update(HttpServletRequest request,
                       @PathVariable Long id,
                       @RequestParam("price") BigDecimal price) {
        userHoldingService.update(parseLong(request.getHeader("requestId")), id, price);
    }

    /**
     * 특정 보유 상품 삭제
     *
     * @param id 삭제할 보유 상품 id
     */
    @DeleteMapping("/{id}")
    public void delete(HttpServletRequest request,
                       @PathVariable Long id) {
        userHoldingService.delete(parseLong(request.getHeader("requestId")), id);
    }
}
