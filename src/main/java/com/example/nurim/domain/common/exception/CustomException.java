package com.example.nurim.domain.common.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final ErrorCode errorCode;
    private final String message;

    public CustomException(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
  public CustomException(ErrorCode errorCode, String message) {
    this.status = errorCode.getStatus();
    this.errorCode = errorCode;
    this.message = errorCode.getMessage() + " " + message;
  }
  public CustomException(Exception ex){
      if(ex.getClass() == CustomException.class){
        CustomException customException = (CustomException) ex;
        this.status = customException.getStatus();
        this.errorCode = customException.getErrorCode();
        this.message = customException.getMessage();
      }else {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.errorCode = ErrorCode.UNKNOWN;
        this.message = "알 수 없는 오류 발생";
        log.error("",ex);
      }
  }
}
