package com.example.nurim.domain.review.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {

    private final Long id;
    private final Long userId;
    private final String name;
    private final double rating;
    private final String contents;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    public ReviewResponseDto(Long id, Long userId, String name, double rating, String contents, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.rating = rating;
        this.contents = contents;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
