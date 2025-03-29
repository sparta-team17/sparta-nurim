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
    PROGRAM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 프로그램입니다."),

    PROGRAM_FULL(HttpStatus.BAD_REQUEST, "해당 프로그램의 신청이 마감되었습니다."),
    PROGRAM_DATE_CLOSED(HttpStatus.BAD_REQUEST, "신청기간이 종료되었습니다."),
    PROGRAM_DATE_NOT_OPENED(HttpStatus.BAD_REQUEST, "신청기간이 시작되지 않았습니다."),

    // PROGRAM_DATE
    PROGRAMDATE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 일정입니다."),
    PROGRAM_DATE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 프로그램 일정입니다."),

    // APPLICATION
    APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 신청입니다."),
    DUPLICATE_APPLICATION(HttpStatus.BAD_REQUEST, "이미 신청한 내역이 존재합니다."),
    APPLICATION_NOT_OWNER(HttpStatus.FORBIDDEN, "해당 신청을 한 유저가 아닙니다."),
    APPLICATION_FULL(HttpStatus.BAD_REQUEST, "선착순 신청 인원이 모두 찼습니다."),
    CANCELLATION_PERIOD_EXPIRED(HttpStatus.BAD_REQUEST, "취소 가능 기간이 종료되었습니다."),
    APPLICATION_CANCELLED(HttpStatus.BAD_REQUEST, "취소한 신청입니다."),

    // CATEGORY
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),

    // REVIEW
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 후기입니다."),
    PROGRAM_NOT_STARTED(HttpStatus.BAD_REQUEST, "아직 프로그램이 시작되지 않았습니다."),
    EXIST_REVIEW(HttpStatus.BAD_REQUEST, "이미 작성한 후기입니다."),
    SORT_METHOD_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "지원하지 않는 정렬 방식입니다."),
    NOT_REVIEW_OWNER(HttpStatus.FORBIDDEN, "해당 후기에 대한 권한이 없습니다."),

    // LOCK
    LOCK_TIMEOUT(HttpStatus.CONFLICT, "락을 획득하는 데 시간이 초가되었습니다."),
    LOCK_KEY_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "락 키가 설정되지 않았습니다."),

    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.");
    private final HttpStatus status;
    private final String message;
}
