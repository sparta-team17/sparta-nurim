package com.example.nurim.domain.application.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApplicationRequestDto {
    private String programTitle;
    private LocalDateTime useDate;
}
