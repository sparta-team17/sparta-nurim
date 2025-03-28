package com.example.nurim.domain.application.controller;

import com.example.nurim.domain.application.dto.response.ApplicationResponseDto;
import com.example.nurim.domain.application.service.ApplicationService;
import com.example.nurim.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/program-date/{programDateId}")
public class ApplicationController {

    private final ApplicationService applicationService;

    // 신청하기
    @PostMapping("/applications")
    public ResponseEntity<ApplicationResponseDto> createApplication(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long programDateId
    ) {
        return ResponseEntity.ok(applicationService.createApplication(authUser, programDateId));
    }

    // 신청취소
    @DeleteMapping
    public void cancelApplication(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long programDateId,
            @RequestParam Long applicationId
    ) {
        applicationService.cancelApplication(authUser, programDateId, applicationId);
    }
}
