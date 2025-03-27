package com.example.nurim.domain.auth.service;

import com.example.nurim.domain.auth.entity.RefreshToken;
import com.example.nurim.domain.auth.exception.AuthException;
import com.example.nurim.domain.auth.repository.RefreshTokenRepository;
import com.example.nurim.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    public String createRefreshToken(User user) {
        String newToken = UUID.randomUUID().toString();
        LocalDateTime expiredAt = LocalDateTime.now().plus(expiration, ChronoUnit.MILLIS);

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId())
                .orElse(new RefreshToken(newToken, expiredAt, user));

        if (refreshToken.isNew()) {
            refreshTokenRepository.save(refreshToken);
        }
        else {
            refreshToken.update(newToken, expiredAt);
        }

        return newToken;
    }

    @Transactional(readOnly = true)
    public User extractUser(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findValidTokenWithUser(token)
                .orElseThrow(() -> new AuthException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));
        return refreshToken.getUser();
    }
}
