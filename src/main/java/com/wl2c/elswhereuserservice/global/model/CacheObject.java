package com.wl2c.elswhereuserservice.global.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class CacheObject<T> {
    private final Instant expiresAt;
    private final T value;
}