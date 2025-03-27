package com.example.nurim.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // USER
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 유저입니다."),

    // NOTICE
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 공지사항입니다"),
    NOT_POST_OWNER(HttpStatus.UNAUTHORIZED,"해당 글의 작성자가 아닙니다."),

    // PROGRAM
    PROGRAM_DATE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 프로그램 일정입니다."),
    PROGRAM_FULL(HttpStatus.BAD_REQUEST, "해당 프로그램의 신청이 마감되었습니다."),
    PROGRAM_DATE_CLOSED(HttpStatus.BAD_REQUEST, "신청기간이 종료되었습니다."),

    // APPLICATION
    APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 신청입니다."),
    DUPLICATE_APPLICATION(HttpStatus.BAD_REQUEST, "이미 신청한 내역이 존재합니다."),
    APPLICATION_NOT_OWNER(HttpStatus.FORBIDDEN, "해당 신청을 한 유저가 아닙니다."),
    APPLICATION_FULL(HttpStatus.BAD_REQUEST, "선착순 신청 인원이 모두 찼습니다."),
    CANCELLATION_PERIOD_EXPIRED(HttpStatus.BAD_REQUEST, "취소 가능 기간이 종료되었습니다."),

    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.");
    private final HttpStatus status;
    private final String message;
}
