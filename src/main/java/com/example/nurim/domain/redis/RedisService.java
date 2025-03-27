package com.example.nurim.domain.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
}
