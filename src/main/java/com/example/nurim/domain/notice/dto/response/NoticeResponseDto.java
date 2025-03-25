package com.example.nurim.domain.notice.dto.response;

import com.example.nurim.domain.notice.dto.request.NoticeRequestDto;
import com.example.nurim.domain.notice.entity.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeResponseDto {
    private Long noticeId;
    private Long userId;
    private String name;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deleteAt;

    public static NoticeResponseDto fromEntity(Notice notice){
        return NoticeResponseDto.builder()
                .noticeId(notice.getId())
                .userId(notice.getUser().getId())
                .name(notice.getUser().getName())
                .title(notice.getTitle())
                .contents(notice.getContents())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .deleteAt(notice.getDeletedAt())
                .build();
    }
}
