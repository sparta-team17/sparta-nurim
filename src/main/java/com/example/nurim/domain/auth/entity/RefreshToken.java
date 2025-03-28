package com.example.nurim.domain.auth.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("token")
@Getter
public class RefreshToken {
    @Id
    private String token;
    @TimeToLive
    private Long expiresIn;
    private UserInfo userInfo;

    public RefreshToken(String token, Long expiresIn, UserInfo userInfo) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.userInfo = userInfo;
    }
}
