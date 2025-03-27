package com.example.nurim.domain.review.controller;

import com.example.nurim.domain.common.dto.AuthUser;
import com.example.nurim.domain.review.dto.request.ReviewSaveRequestDto;
import com.example.nurim.domain.review.dto.request.ReviewUpdateRequestDto;
import com.example.nurim.domain.review.dto.response.ReviewResponseDto;
import com.example.nurim.domain.review.dto.response.ReviewSaveResponseDto;
import com.example.nurim.domain.review.dto.response.ReviewUpdateResponseDto;
import com.example.nurim.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/applications/{applicationId}/reviews")
    public ResponseEntity<ReviewSaveResponseDto> createReview(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("applicationId") Long applicationId,
            @Valid @RequestBody ReviewSaveRequestDto request
    ) {
        return new ResponseEntity<>(reviewService.createReview(authUser.getId(), applicationId, request), HttpStatus.CREATED);
    }

    @GetMapping("/programs/{programId}/reviews")
    public ResponseEntity<Page<ReviewResponseDto>> findAllReview(
            @PathVariable("programId") Long programId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "latest") String sortBy
    ) {
        return ResponseEntity.ok(reviewService.findAllReview(programId, page, size, sortBy));
    }

    @GetMapping("/programs/{programId}/reviews/{reviewId}")
    public ResponseEntity<ReviewResponseDto> findOneReview(
            @PathVariable("programId") Long programId,
            @PathVariable("reviewId") Long reviewId
    ) {
        return ResponseEntity.ok(reviewService.findOneReview(programId, reviewId));
    }

    @PatchMapping("/programs/{programId}/reviews/{reviewId}")
    public ResponseEntity<ReviewUpdateResponseDto> updateReview(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("programId") Long programId,
            @PathVariable("reviewId") Long reviewId,
            @Valid @RequestBody ReviewUpdateRequestDto request
    ) {
        return ResponseEntity.ok(reviewService.updateReview(authUser.getId(), programId, reviewId, request));
    }

    @DeleteMapping("/programs/{programId}/reviews/{reviewId}")
    public void deleteReview(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable("programId") Long programId,
            @PathVariable("reviewId") Long reviewId
    ) {
        reviewService.deleteReview(authUser.getId(), programId, reviewId);
    }
}
