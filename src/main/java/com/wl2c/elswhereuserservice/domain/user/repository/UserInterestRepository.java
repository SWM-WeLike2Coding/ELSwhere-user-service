package com.wl2c.elswhereuserservice.domain.user.repository;

import com.wl2c.elswhereuserservice.domain.user.model.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserInterestRepository extends JpaRepository<Interest, Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from Interest i " +
            "where i.id = :id and i.user.id = :userId ")
    void deleteInterest(@Param("userId") Long userId,
                       @Param("id") Long id);

    @Query("select i from Interest i where i.user.id = :userId ")
    List<Interest> findAllByUserId(@Param("userId") Long userId);

    @Query("select i from Interest i " +
            "where i.user.id = :userId " +
            "and i.productId = :productId ")
    Optional<Interest> findByUserIdAndProductId(@Param("userId") Long userId,
                                                @Param("productId") Long productId);
}
