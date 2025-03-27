package com.example.nurim.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GoneException extends RuntimeException{

    private final HttpStatus status = HttpStatus.GONE;

    public GoneException(String message) {
        super(message);
    }
}
