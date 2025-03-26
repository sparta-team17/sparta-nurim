package com.example.nurim.domain.notice.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeSearchResponseDto {
    private Long noticeId;
    private String title;
    private LocalDateTime createdAt;
    private String userName;

    @QueryProjection
    public NoticeSearchResponseDto(Long noticeId, String title, LocalDateTime createdAt, String userName) {
        this.noticeId = noticeId;
        this.title = title;
        this.createdAt = createdAt;
        this.userName = userName;
    }
}
