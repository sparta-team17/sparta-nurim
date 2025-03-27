package com.example.nurim.domain.auth.entity;

import com.example.nurim.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public RefreshToken(String token, LocalDateTime expiredAt, User user) {
        this.token = token;
        this.expiredAt = expiredAt;
        this.user = user;
    }

    public boolean isNew() {
        return id == null;
    }

    public void update(String newToken, LocalDateTime expiredAt) {
        this.token = newToken;
        this.expiredAt = expiredAt;
    }
}
