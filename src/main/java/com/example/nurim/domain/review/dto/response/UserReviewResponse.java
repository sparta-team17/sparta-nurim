package com.example.nurim.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserReviewResponse {
    private final Long reviewId;
    private final String contents;
    private final Double rating;
    private final LocalDateTime createdAt;
    private final Long programId;
    private final String programTitle;
    private final LocalDateTime programDeletedAt;
}
