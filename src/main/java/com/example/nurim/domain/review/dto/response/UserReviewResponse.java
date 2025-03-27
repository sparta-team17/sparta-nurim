package com.example.nurim.domain.review.dto.response;

import java.time.LocalDateTime;

public interface UserReviewResponse {
    Long getReviewId();
    String getContents();
    Double getRating();
    LocalDateTime getCreatedAt();
    Long getProgramId();
    String getProgramTitle();
    LocalDateTime getProgramDeletedAt();
}
