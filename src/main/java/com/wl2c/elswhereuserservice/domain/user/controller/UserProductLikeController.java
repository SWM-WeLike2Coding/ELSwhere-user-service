package com.wl2c.elswhereuserservice.domain.user.controller;

import com.wl2c.elswhereuserservice.client.product.dto.response.ResponseSummarizedProductDto;
import com.wl2c.elswhereuserservice.domain.user.service.UserProductLikeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.lang.Long.parseLong;

@Tag(name = "사용자의 상품 좋아요", description = "사용자의 상품 좋아요 관련 api")
@RestController
@RequestMapping("/v1/product/like")
@RequiredArgsConstructor
public class UserProductLikeController {

    private final UserProductLikeService userProductLikeService;

    /**
     * 사용자 DB에 해당 상품에 대한 사용자의 '좋아요' 기록이 되어있는지 확인 (프론트 사용x)
     *
     * @param id 상품 id
     */
    @GetMapping("/{id}")
    public void checkIsLiked(HttpServletRequest request,
                             @PathVariable Long id) {
        userProductLikeService.findByProductIdAndUserId(id, parseLong(request.getHeader("requestId")));
    }

    /**
     * 사용자가 좋아요한 상품 리스트 조회
     *
     * @return 사용자가 좋아요한 관심 상품 리스트
     */
    @GetMapping
    public List<ResponseSummarizedProductDto> findLikedProducts(HttpServletRequest request) {
        return userProductLikeService.findLikedProducts(parseLong(request.getHeader("requestId")));
    }
}
