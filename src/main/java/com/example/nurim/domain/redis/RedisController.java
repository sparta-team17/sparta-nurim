package com.example.nurim.domain.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;

    // Redis에 데이터 저장
    @PostMapping("/save")
    public String saveData(@RequestParam String key, @RequestParam String value) {
        redisService.saveData(key, value);
        return "Data saved to Redis with key: " + key;
    }

    // Redis에서 데이터 조회
    @GetMapping("/get")
    public String getData(@RequestParam String key) {
        String value = redisService.getData(key);
        return value != null ? "Value for key '" + key + "': " + value : "No data found for key: " + key;
    }
}
