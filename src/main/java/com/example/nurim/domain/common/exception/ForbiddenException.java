package com.example.nurim.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ForbiddenException extends RuntimeException {

  private final HttpStatus status = HttpStatus.FORBIDDEN;

  public ForbiddenException(String message) {
    super(message);
  }
}
