package com.example.nurim.domain.notice.controller;

import com.example.nurim.domain.notice.dto.response.NoticeResponseDto;
import com.example.nurim.domain.notice.dto.response.NoticeSearchResponseDto;
import com.example.nurim.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/notices/{noticeId}")
    public ResponseEntity<NoticeResponseDto> findNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.findNotice(noticeId));
    }

    @GetMapping("/notices")
    public ResponseEntity<Page<NoticeSearchResponseDto>> findNotices(@RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(noticeService.findNotices(page, size, keyword));
    }

}
