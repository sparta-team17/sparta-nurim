package com.example.nurim.domain.program.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProgramException extends RuntimeException{

  private final HttpStatus status;

  public ProgramException(String message, HttpStatus status){
    super(message);
    this.status = status;
  }
}
