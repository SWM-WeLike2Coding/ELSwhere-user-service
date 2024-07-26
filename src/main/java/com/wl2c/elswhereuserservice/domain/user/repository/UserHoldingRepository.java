package com.wl2c.elswhereuserservice.domain.user.repository;

import com.wl2c.elswhereuserservice.domain.user.model.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHoldingRepository extends JpaRepository<Holding, Long> {


}
