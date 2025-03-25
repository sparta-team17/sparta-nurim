package com.example.nurim.config;

import com.example.nurim.domain.program.exception.ProgramException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, String message){
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.name());
        errorResponse.put("code", status.value());
        errorResponse.put("message", message);

        return new ResponseEntity<>(errorResponse, status);

    }

    @ExceptionHandler(ProgramException.class)
    public ResponseEntity<Map<String, Object>> handleProgramException(ProgramException ex) {
        return getErrorResponse(ex.getStatus(), ex.getMessage());
    }
}
