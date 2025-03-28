package com.example.nurim.domain.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class CustomExceptionResponse {
    private final LocalDateTime occurDateTime;
    private final HttpStatus statusCode;
    private final int status;
    private final String message;

    public static CustomExceptionResponse toResponse(ErrorCode errorCode) {
        return new CustomExceptionResponse(
                LocalDateTime.now(),
                errorCode.getStatus(),
                errorCode.getStatus().value(),
                errorCode.getMessage()
        );
    }
}
