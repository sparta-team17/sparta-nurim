package com.example.nurim.domain.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public CustomExceptionResponse handleCustomException(CustomException e) {
        return CustomExceptionResponse.toResponse(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public CustomExceptionResponse handleAllExceptions(Exception e) {
        return CustomExceptionResponse.toResponse(ErrorCode.UNKNOWN);
    }
  
}
