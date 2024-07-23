package com.wl2c.elswhereuserservice.domain.user.model;

public enum UserStatus {
    /**
     * 활성화
     */
    ACTIVE,

    /**
     * 비활성화
     */
    INACTIVE;

    public boolean isActive() {
        return this == ACTIVE;
    }
}
