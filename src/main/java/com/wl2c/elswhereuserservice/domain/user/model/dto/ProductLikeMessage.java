package com.wl2c.elswhereuserservice.domain.user.model.dto;

import com.wl2c.elswhereuserservice.domain.user.model.entity.LikeState;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ProductLikeMessage {

    @NotNull
    private Long userId;

    @NotNull
    private Long productId;

    @NotNull
    private LikeState likeState;

    @Builder
    private ProductLikeMessage(Long userId,
                               Long productId,
                               LikeState likeState) {
        this.userId = userId;
        this.productId = productId;
        this.likeState = likeState;
    }
}
