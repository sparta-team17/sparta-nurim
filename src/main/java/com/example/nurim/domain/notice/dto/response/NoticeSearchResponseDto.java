package com.example.nurim.domain.notice.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeSearchResponseDto {
    private final Long noticeId;
    private final String title;
    private final Integer count;
    private final LocalDateTime createdAt;
    private final String userName;

    @QueryProjection
    public NoticeSearchResponseDto(Long noticeId, String title,Integer count, LocalDateTime createdAt, String userName) {
        this.noticeId = noticeId;
        this.title = title;
        this.count = count;
        this.createdAt = createdAt;
        this.userName = userName;
    }
}
