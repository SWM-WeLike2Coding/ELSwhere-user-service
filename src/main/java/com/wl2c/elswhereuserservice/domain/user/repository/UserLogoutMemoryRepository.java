package com.wl2c.elswhereuserservice.domain.user.repository;

import java.time.Duration;

public interface UserLogoutMemoryRepository {

    void set(String key, Object object, Duration duration);

}
