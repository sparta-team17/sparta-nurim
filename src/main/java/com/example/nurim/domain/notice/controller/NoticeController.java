package com.example.nurim.domain.notice.controller;

import com.example.nurim.domain.common.dto.AuthUser;
import com.example.nurim.domain.notice.dto.request.NoticeRequestDto;
import com.example.nurim.domain.notice.dto.response.NoticeResponseDto;
import com.example.nurim.domain.notice.dto.response.NoticeSearchResponseDto;
import com.example.nurim.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping("/admin/notices")
    public ResponseEntity<NoticeResponseDto> createNotice(@RequestBody NoticeRequestDto requestDto){
        Long userId = 1L;
        return new ResponseEntity<>(noticeService.createNotice(userId, requestDto.getTitle(), requestDto.getContents()), HttpStatus.CREATED);
    }

    @PatchMapping("/admin/notices/{noticeId}")
    public ResponseEntity<NoticeResponseDto> updateNotice(@PathVariable Long noticeId,
                                                          @RequestBody NoticeRequestDto requestDto){
        Long userId = 1L;
        return ResponseEntity.ok(noticeService.updateNotice(userId, noticeId, requestDto.getTitle(), requestDto.getContents()));
    }

    @DeleteMapping("/admin/notices/{noticeId}")
    public ResponseEntity<NoticeResponseDto> deleteNotice(@PathVariable Long noticeId){
        Long userId = 1L;
        return ResponseEntity.ok(noticeService.deleteNotice(userId, noticeId));
    }

    @GetMapping("/notices/{noticeId}")
    public ResponseEntity<NoticeResponseDto> findNotice(@PathVariable Long noticeId){
        return ResponseEntity.ok(noticeService.findNotice(noticeId));
    }

    @GetMapping("/notices")
    public ResponseEntity<Page<NoticeSearchResponseDto>> findNotices(@RequestParam(defaultValue = "1") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(noticeService.findNotices(page, size, keyword));
    }

}
