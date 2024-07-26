package com.wl2c.elswhereuserservice.domain.user.repository;

import com.wl2c.elswhereuserservice.domain.user.model.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface UserHoldingRepository extends JpaRepository<Holding, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update Holding h set h.price = :price " +
            "where h.id = :id and h.user.id = :userId ")
    void updateHolding(@Param("userId") Long userId,
                       @Param("id") Long id,
                       @Param("price") BigDecimal price);

    @Modifying(clearAutomatically = true)
    @Query("delete from Holding h " +
            "where h.id = :id and h.user.id = :userId ")
    void deleteHolding(@Param("userId") Long userId,
                       @Param("id") Long id);
}
