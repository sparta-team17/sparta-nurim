package com.example.nurim.domain.auth.service;

import com.example.nurim.domain.auth.entity.RefreshToken;
import com.example.nurim.domain.auth.entity.UserInfo;
import com.example.nurim.domain.auth.repository.RefreshTokenRepository;
import com.example.nurim.domain.common.exception.CustomException;
import com.example.nurim.domain.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration}")
    private Long expiration;

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public String createRefreshToken(UserInfo userInfo) {
        String newToken = UUID.randomUUID().toString();
        LocalDateTime expiredAt = LocalDateTime.now().plus(expiration, ChronoUnit.MILLIS);

        RefreshToken refreshToken = new RefreshToken(newToken, expiredAt, userInfo);
        refreshTokenRepository.save(refreshToken);

        return newToken;
    }

    @Transactional(readOnly = true)
    public UserInfo extractUserInfo(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findById(token)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));
        return refreshToken.getUserInfo();
    }
}
