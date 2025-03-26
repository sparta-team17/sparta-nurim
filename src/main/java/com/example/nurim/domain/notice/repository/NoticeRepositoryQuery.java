package com.example.nurim.domain.notice.repository;

import com.example.nurim.domain.notice.dto.response.NoticeResponseDto;
import com.example.nurim.domain.notice.dto.response.NoticeSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryQuery {
    Page<NoticeSearchResponseDto> findNotices(String keyword, Pageable pageable);
}
