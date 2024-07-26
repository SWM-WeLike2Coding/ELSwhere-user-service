package com.wl2c.elswhereuserservice.domain.user.controller;

import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestCreateHoldingDto;
import com.wl2c.elswhereuserservice.domain.user.service.UserHoldingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
     */
    @PostMapping
    public void create(HttpServletRequest request,
                       @Valid @RequestBody RequestCreateHoldingDto dto) {
        userHoldingService.create(parseLong(request.getHeader("requestId")), dto);
    }

}
