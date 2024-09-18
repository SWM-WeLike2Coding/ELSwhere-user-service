package com.wl2c.elswhereuserservice.domain.user.repository;

import com.wl2c.elswhereuserservice.domain.user.model.entity.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserProductLikeRepository extends JpaRepository<ProductLike, Long> {

    @Query("select pl from ProductLike pl " +
            "where pl.productId = :productId " +
            "and pl.user.id = :userId ")
    Optional<ProductLike> findByProductIdAndUserId(@Param("productId") Long productId,
                                                   @Param("userId") Long userId);

    @Query("select pl from ProductLike pl " +
            "where pl.user.id = :userId ")
    List<ProductLike> findAllByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("delete from ProductLike pl " +
            "where pl.productId = :productId " +
            "and pl.user.id = :userId ")
    void deleteByProductIdAndUserId(@Param("productId") Long productId,
                                    @Param("userId") Long userId);

}
