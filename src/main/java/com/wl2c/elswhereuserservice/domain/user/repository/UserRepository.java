package com.wl2c.elswhereuserservice.domain.user.repository;

import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.userStatus = 'ACTIVE' and u.socialId = :socialId ")
    Optional<User> findBySocialId(@Param("socialId") String socialId);

    @Query("select u from User u where u.nickname = :nickname")
    Optional<User> findByNickname(@Param("nickname") String nickname);

    @Modifying(clearAutomatically = true)
    @Query("delete from User u " +
            "where u.userStatus = 'INACTIVE' " +
            "and FUNCTION('DATEDIFF', u.lastModifiedAt, u.createdAt) >= :period ")
    void deleteAllByWithdrawnUser(@Param("period") Long period);
}
