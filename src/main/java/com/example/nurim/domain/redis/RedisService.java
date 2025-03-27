package com.example.nurim.domain.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 데이터를 Redis에 저장
    public void saveData(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // Redis에서 데이터 조회
    public String getData(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    // 조회수 증가
    public void incrementViewCount(String key) {
        // Redis에서 조회수 증가
        redisTemplate.opsForValue().increment(key, 1);
    }

    // 조회수 가져오기
    public Long getViewCount(String key) {
        // Redis에서 값 가져오기
        Object value = redisTemplate.opsForValue().get(key);
        // 값이 null이면 0 반환, 아니면 해당 값 반환
        return value != null ? Long.valueOf(value.toString()) : 0L;
    }

    // Redis에 조회수 저장 (TTL 설정)
    public void saveDataWithTTL(String key, String value, long ttlInSeconds) {
        // Redis에 값 저장, TTL 설정
        redisTemplate.opsForValue().set(key, value, ttlInSeconds, TimeUnit.SECONDS);
    }
}