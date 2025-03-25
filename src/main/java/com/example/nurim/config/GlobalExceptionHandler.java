package com.example.nurim.config;

import com.example.nurim.domain.auth.exception.AuthException;
import com.example.nurim.domain.user.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleUserException(UserException e) {
        return getErrorResponse(e.getStatus(), e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleAuthException(AuthException e) {
        return getErrorResponse(e.getStatus(), e.getMessage());
    }

    public ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = ErrorResponseUtil.getErrorResponse(status, message);
        return new ResponseEntity<>(errorResponse, status);
    }
}
