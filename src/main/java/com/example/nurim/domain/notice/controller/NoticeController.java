package com.example.nurim.domain.notice.controller;

import com.example.nurim.domain.common.dto.AuthUser;
import com.example.nurim.domain.notice.dto.request.NoticeRequestDto;
import com.example.nurim.domain.notice.dto.response.NoticeResponseDto;
import com.example.nurim.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
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
        return new ResponseEntity<>(noticeService.updateNotice(userId, noticeId, requestDto.getTitle(), requestDto.getContents()),HttpStatus.OK);
    }
}
