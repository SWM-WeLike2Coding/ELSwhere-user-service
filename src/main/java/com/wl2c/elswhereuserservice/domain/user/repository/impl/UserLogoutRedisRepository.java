package com.wl2c.elswhereuserservice.domain.user.repository.impl;

import com.wl2c.elswhereuserservice.domain.user.repository.UserLogoutMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class UserLogoutRedisRepository implements UserLogoutMemoryRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void set(String key, Object object, Duration expirationDuration) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(object.getClass()));
        redisTemplate.opsForValue().set(key, object, expirationDuration);
    }
}
