package com.example.nurim.domain.notice.controller;

import com.example.nurim.domain.common.dto.AuthUser;
import com.example.nurim.domain.notice.dto.response.NoticeResponseDto;
import com.example.nurim.domain.notice.dto.response.NoticeSearchResponseDto;
import com.example.nurim.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/v1/{noticeId}")
    public ResponseEntity<NoticeResponseDto> findNoticeWithDb(@PathVariable Long noticeId,
                                                              @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(noticeService.findNoticeWithDb(noticeId,authUser.getId()));
    }

    @GetMapping("/v2/{noticeId}")
    public ResponseEntity<NoticeResponseDto> findNoticeWithCache(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.findNoticeWithCache(noticeId));
    }

    @GetMapping
    public ResponseEntity<Page<NoticeSearchResponseDto>> findNotices(@RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(noticeService.findNotices(page, size, keyword));
    }

}
