package com.example.nurim.domain.auth.repository;

import com.example.nurim.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long userId);
    @Query("SELECT r FROM RefreshToken r JOIN FETCH r.user WHERE r.token = :token AND r.expiredAt > CURRENT_TIMESTAMP")
    Optional<RefreshToken> findValidTokenWithUser(@Param("token") String token);
}
