package com.example.nurim.domain.application.controller;

import com.example.nurim.domain.application.dto.request.ApplicationRequestDto;
import com.example.nurim.domain.application.dto.response.ApplicationResponseDto;
import com.example.nurim.domain.application.service.ApplicationService;
import com.example.nurim.domain.common.dto.AuthUser;
import jakarta.validation.Valid;
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
            @PathVariable Long programDateId,
            @RequestBody @Valid ApplicationRequestDto requestDto
    ) {
        return ResponseEntity.ok(applicationService.createApplication(authUser, programDateId, requestDto));
    }

    // 신청취소
    @DeleteMapping
    public void deleteApplication(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long programDateId,
            @PathVariable Long applicationId
    ) {
        applicationService.deleteApplication(authUser, programDateId, applicationId);
    }
}
