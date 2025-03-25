package com.example.nurim.domain.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserApplicationResponse {
    private final Long applicationId;
    private final LocalDateTime applicationCreatedAt;
    private final LocalDateTime usageDateTime;
    private final Long programId;
    private final String programTitle;
    private final LocalDateTime programDeletedAt;
}
