package com.example.nurim.domain.review.service;

import com.example.nurim.domain.application.entity.Application;
import com.example.nurim.domain.application.enums.ApplicationStatus;
import com.example.nurim.domain.application.repository.ApplicationRepository;
import com.example.nurim.domain.common.dto.AuthUser;
import com.example.nurim.domain.common.exception.*;
import com.example.nurim.domain.program.entity.Program;
import com.example.nurim.domain.program.repository.ProgramRepository;
import com.example.nurim.domain.review.dto.request.ReviewSaveRequestDto;
import com.example.nurim.domain.review.dto.request.ReviewUpdateRequestDto;
import com.example.nurim.domain.review.dto.response.ReviewResponseDto;
import com.example.nurim.domain.review.dto.response.ReviewSaveResponseDto;
import com.example.nurim.domain.review.dto.response.ReviewUpdateResponseDto;
import com.example.nurim.domain.review.entity.Review;
import com.example.nurim.domain.review.repository.ReviewRepository;
import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserRepository userRepository;
    private final ProgramRepository programRepository;
    private final ApplicationRepository applicationRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewSaveResponseDto createReview(AuthUser authUser, Long programId, ReviewSaveRequestDto request) {

        Program findProgram = programRepository.findById(programId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "존재하지 않는 프로그램입니다."));

        validateDeletedProgram(findProgram);

        User findUser = userRepository.findById(authUser.getId()).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."));

        Application findApplication = applicationRepository.findApplicationByUserIdAndProgramId(findUser.getId(), programId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "존재하지 않는 신청입니다."));

        validateApplicationCancelled(findApplication);

        validateProgramStarted(findApplication);

        Review review = Review.builder()
                .rating(request.getRating())
                .contents(request.getContents())
                .user(findUser)
                .program(findProgram)
                .build();

        Review savedReview = reviewRepository.save(review);

        return ReviewSaveResponseDto.builder()
                .id(savedReview.getId())
                .userId(savedReview.getUser().getId())
                .name(savedReview.getUser().getName())
                .rating(savedReview.getRating())
                .contents(savedReview.getContents())
                .createdAt(savedReview.getCreatedAt())
                .updatedAt(savedReview.getUpdatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponseDto> findAllReview(Long programId, int page, int size, String sortBy) {

        Program findProgram = programRepository.findById(programId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "존재하지 않는 프로그램입니다."));

        validateDeletedProgram(findProgram);

        Pageable pageable = PageRequest.of(page - 1, size, getSortByCriteria(sortBy));

        // 삭제된 리뷰를 제외한 모든 리뷰 조회
        Page<Review> findReviews = reviewRepository.findReviewsByProgramId(programId, pageable);

        return findReviews.map(review -> new ReviewResponseDto(
                        review.getId(),
                        review.getUser().getId(),
                        review.getUser().getName(),
                        review.getRating(),
                        review.getContents(),
                        review.getCreatedAt(),
                        review.getUpdatedAt()));
    }

    @Transactional(readOnly = true)
    public ReviewResponseDto findOneReview(Long programId, Long reviewId) {

        Program findProgram = programRepository.findById(programId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "존재하지 않는 프로그램입니다."));

        validateDeletedProgram(findProgram);

        Review findReview = reviewRepository.findById(reviewId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "존재하지 않는 후기입니다."));

        validateDeletedReview(findReview);

        return ReviewResponseDto.builder()
                .id(findReview.getId())
                .userId(findReview.getUser().getId())
                .name(findReview.getUser().getName())
                .rating(findReview.getRating())
                .contents(findReview.getContents())
                .createdAt(findReview.getCreatedAt())
                .updatedAt(findReview.getUpdatedAt())
                .build();
    }

    @Transactional
    public ReviewUpdateResponseDto updateReview(AuthUser authUser, Long programId, Long reviewId, ReviewUpdateRequestDto request) {

        Program findProgram = programRepository.findById(programId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "존재하지 않는 프로그램입니다."));

        validateDeletedProgram(findProgram);

        validateReviewPermission(authUser, programId);

        Review findReview = reviewRepository.findById(reviewId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "존재하지 않는 후기입니다."));

        validateDeletedReview(findReview);

        findReview.updateReview(request.getRating(), request.getContents());

        Review savedReview = reviewRepository.save(findReview);

        return ReviewUpdateResponseDto.builder()
                .id(savedReview.getId())
                .userId(savedReview.getUser().getId())
                .name(savedReview.getUser().getName())
                .rating(savedReview.getRating())
                .contents(savedReview.getContents())
                .createdAt(savedReview.getCreatedAt())
                .updatedAt(savedReview.getUpdatedAt())
                .build();
    }

    @Transactional
    public void deleteReview(AuthUser authUser, Long programId, Long reviewId) {
        Program findProgram = programRepository.findById(programId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "존재하지 않는 프로그램입니다."));

        validateDeletedProgram(findProgram);

        validateReviewPermission(authUser, programId);

        Review findReview = reviewRepository.findById(reviewId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "존재하지 않는 후기입니다."));

        validateDeletedReview(findReview);

        findReview.deleteReview();
    }

    private void validateDeletedProgram(Program findProgram) {
        if (findProgram.getDeletedAt() != null) {
            throw new GoneException(HttpStatus.GONE, "이미 삭제된 프로그램입니다."); // Gone을 사용해도 되는지, 메시지 내용에 대해서 질문
        }
    }

    private void validateReviewPermission(AuthUser authUser, Long programId) {
        if (!applicationRepository.existsByUserIdAndProgramId(authUser.getId(), programId)) {
            throw new ForbiddenException(HttpStatus.FORBIDDEN, "해당 후기에 대한 권한이 없습니다.");
        }
    }

    private void validateDeletedReview(Review findReview) {
        if (findReview.getDeletedAt() != null) {
            throw new GoneException(HttpStatus.GONE, "이미 삭제된 후기입니다."); // Gone을 사용해도 되는지, 메시지 내용에 대해서 질문
        }
    }

    private void validateApplicationCancelled(Application findApplication) {
        if (findApplication.getStatus().equals(ApplicationStatus.CANCLE)) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "취소한 신청입니다.");
        }
    }

    private void validateProgramStarted(Application findApplication) {
        // 리뷰한 날짜가 이용 시작일보다 전이면 예외
        if (findApplication.getProgramDate().getProgram().getUsageStartDate().isAfter(LocalDateTime.now())) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "아직 프로그램이 시작되지 않았습니다.");
        }
    }

    private Sort getSortByCriteria(String sortBy) {
        return switch (sortBy) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            case "rating" -> Sort.by(Sort.Direction.DESC, "rating");
            default -> throw new IllegalArgumentException("지원하지 않는 정렬 방식입니다.");
        };
    }
}
