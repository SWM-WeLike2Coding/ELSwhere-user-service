package com.wl2c.elswhereuserservice.domain.user.repository;

import com.wl2c.elswhereuserservice.domain.user.model.entity.InvestmentPropensity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserInvestmentPropensityRepository extends JpaRepository<InvestmentPropensity, Long> {

    @Query("select s from InvestmentPropensity s where s.user.id = :userId ")
    Optional<InvestmentPropensity> findByUserId(@Param("userId") Long userId);
}
