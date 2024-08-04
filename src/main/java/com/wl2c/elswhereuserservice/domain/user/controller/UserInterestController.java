package com.wl2c.elswhereuserservice.domain.user.controller;

import com.wl2c.elswhereuserservice.domain.user.model.dto.request.RequestCreateInterestDto;
import com.wl2c.elswhereuserservice.domain.user.model.dto.list.SummarizedUserInterestDto;
import com.wl2c.elswhereuserservice.domain.user.service.UserInterestService;
import com.wl2c.elswhereuserservice.global.model.dto.ResponseIdDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.lang.Long.parseLong;

@Tag(name = "사용자의 관심 상품", description = "사용자의 관심 상품 관련 api")
@RestController
@RequestMapping("/v1/interest")
@RequiredArgsConstructor
public class UserInterestController {

    private final UserInterestService userInterestService;

    /**
     * 관심 상품 등록
     *
     * @param dto 등록하고자 하는 상품 id
     * @return 관심 상품 id
     */
    @PostMapping
    public ResponseIdDto create(HttpServletRequest request,
                                @Valid @RequestBody RequestCreateInterestDto dto) {
        return userInterestService.create(parseLong(request.getHeader("requestId")), dto);
    }

    /**
     * 사용자의 관심 상품 리스트 조회
     *
     * @return 사용자가 등록한 관심 상품 리스트
     */
    @GetMapping
    public List<SummarizedUserInterestDto> read(HttpServletRequest request) {
        return userInterestService.read(parseLong(request.getHeader("requestId")));
    }

    /**
     * 특정 관심 상품 삭제
     *
     * @param id 삭제할 관심 상품 id
     */
    @DeleteMapping("/{id}")
    public void delete(HttpServletRequest request,
                       @PathVariable Long id) {
        userInterestService.delete(parseLong(request.getHeader("requestId")), id);
    }
}
