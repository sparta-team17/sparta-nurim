package com.example.nurim.config;

import com.example.nurim.domain.auth.exception.AuthException;
import com.example.nurim.domain.user.exception.UserException;
import com.example.nurim.domain.common.exception.BadRequestException;
import com.example.nurim.domain.common.exception.ForbiddenException;
import com.example.nurim.domain.common.exception.GoneException;
import com.example.nurim.domain.common.exception.NotFoundException;
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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex) {
        return getErrorResponse(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(GoneException.class)
    public ResponseEntity<Map<String, Object>> handleGoneException(GoneException ex) {
        return getErrorResponse(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException ex) {
        return getErrorResponse(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, Object>> handleForbiddenException(ForbiddenException ex) {
        return getErrorResponse(ex.getStatus(), ex.getMessage());
    }

    public ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = ErrorResponseUtil.getErrorResponse(status, message);
        return new ResponseEntity<>(errorResponse, status);
    }
}
