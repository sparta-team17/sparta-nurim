package com.example.nurim.domain.notice.controller;

import com.example.nurim.domain.common.annotation.HasAdminRole;
import com.example.nurim.domain.common.annotation.HasUserRole;
import com.example.nurim.domain.common.dto.AuthUser;
import com.example.nurim.domain.notice.dto.request.NoticeRequestDto;
import com.example.nurim.domain.notice.dto.response.NoticeResponseDto;
import com.example.nurim.domain.notice.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/notices")
public class AdminNoticeController {

    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity<NoticeResponseDto> createNotice(@RequestBody @Valid NoticeRequestDto requestDto,
                                                          @AuthenticationPrincipal AuthUser authUser
    ){
        return new ResponseEntity<>(noticeService.createNotice(authUser.getId(), requestDto.getTitle(), requestDto.getContents()), HttpStatus.CREATED);
    }

    @PatchMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDto> updateNotice(@PathVariable Long noticeId,
                                                          @RequestBody @Valid NoticeRequestDto requestDto,
                                                          @AuthenticationPrincipal AuthUser authUser){
        return ResponseEntity.ok(noticeService.updateNotice(authUser.getId(), noticeId, requestDto.getTitle(), requestDto.getContents()));
    }
    @HasAdminRole()
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDto> deleteNotice(@PathVariable Long noticeId,
                                                          @AuthenticationPrincipal AuthUser authUser){
        return ResponseEntity.ok(noticeService.deleteNotice(authUser.getId(), noticeId));
    }

}
