package com.CommerceCore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
    private final StringRedisTemplate redisTemplate;
    public void blacklistToken(String token, long expiryMillis){
        long seconds=expiryMillis/1000;
        redisTemplate.opsForValue().set(
                token,
                "blacklisted",
                seconds,
                TimeUnit.SECONDS
        );
    }
    public Boolean isTokenBlacklisted(String token){
        return redisTemplate.hasKey(token);
    }
}
