package com.example.nurim.domain.user.dto.request;

import com.example.nurim.domain.application.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FindApplicationRequest {
    private final LocalDateTime createdFrom;
    private final LocalDateTime createdTo;
    private final String title;
    private final ApplicationStatus status;
}
