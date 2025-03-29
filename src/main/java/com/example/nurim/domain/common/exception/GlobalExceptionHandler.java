package com.example.nurim.domain.common.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
