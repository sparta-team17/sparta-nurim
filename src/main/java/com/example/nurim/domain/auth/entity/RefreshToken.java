package com.example.nurim.domain.auth.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RedisHash("token")
@Getter
public class RefreshToken {
    @Id
    private String token;
    private LocalDateTime expiredAt;
    private UserInfo userInfo;

    @TimeToLive
    public Long getExpiration() {
        return ChronoUnit.SECONDS.between(LocalDateTime.now(), expiredAt);
    }

    public RefreshToken(String token, LocalDateTime expiredAt, UserInfo userInfo) {
        this.token = token;
        this.expiredAt = expiredAt;
        this.userInfo = userInfo;
    }
}
