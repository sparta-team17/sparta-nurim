package com.example.nurim.domain.review.service;

import com.example.nurim.domain.application.entity.Application;
import com.example.nurim.domain.application.enums.ApplicationStatus;
import com.example.nurim.domain.application.repository.ApplicationRepository;
import com.example.nurim.domain.common.exception.BadRequestException;
import com.example.nurim.domain.common.exception.ForbiddenException;
import com.example.nurim.domain.common.exception.NotFoundException;
import com.example.nurim.domain.program.entity.Program;
import com.example.nurim.domain.program.entity.ProgramDate;
import com.example.nurim.domain.program.repository.ProgramDateRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final UserRepository userRepository;
    private final ProgramRepository programRepository;
    private final ProgramDateRepository programDateRepository;
    private final ApplicationRepository applicationRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewSaveResponseDto createReview(Long userId, Long applicationId, ReviewSaveRequestDto request) {

        Application findApplication = applicationRepository.findById(applicationId).orElseThrow(() ->
                new NotFoundException("존재하지 않는 신청입니다."));

        validateProgramExists(findApplication.getProgramDate().getProgram().getId());

        validateApplicationCancelled(findApplication);

        LocalDateTime now = LocalDateTime.now();
        validateProgramStarted(findApplication, now);

        ProgramDate findProgramDate = programDateRepository.findById(findApplication.getProgramDate().getId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 일정입니다."));

        validateReviewExists(userId, findProgramDate);

        User findUser = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("존재하지 않는 유저입니다."));

        Review review = Review.builder()
                .rating(request.getRating())
                .contents(request.getContents())
                .user(findUser)
                .programDate(findProgramDate)
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

        Program findProgram = programRepository.findProgramByIdAndDeletedAtIsNull(programId).orElseThrow(() ->
                new NotFoundException("존재하지 않는 프로그램입니다."));

        Pageable pageable = PageRequest.of(page - 1, size, getSortByCriteria(sortBy));

        // 삭제된 리뷰를 제외한 모든 리뷰 조회
        Page<Review> findReviews = reviewRepository.findReviewsByProgramId(findProgram.getId(), pageable);

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

        validateProgramExists(programId);

        Review findReview = reviewRepository.findReviewByIdAndDeletedAtIsNull(reviewId).orElseThrow(() ->
                new NotFoundException("존재하지 않는 후기입니다."));

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
    public ReviewUpdateResponseDto updateReview(Long userId, Long programId, Long reviewId, ReviewUpdateRequestDto request) {

        validateProgramExists(programId);

        Review findReview = reviewRepository.findReviewByIdAndDeletedAtIsNull(reviewId).orElseThrow(() ->
                new NotFoundException("존재하지 않는 후기입니다."));

        validateReviewPermission(userId, findReview);

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
    public void deleteReview(Long userId, Long programId, Long reviewId) {

        validateProgramExists(programId);

        Review findReview = reviewRepository.findReviewByIdAndDeletedAtIsNull(reviewId).orElseThrow(() ->
                new NotFoundException("존재하지 않는 후기입니다."));

        validateReviewPermission(userId, findReview);

        LocalDateTime deletedAt = LocalDateTime.now();
        findReview.deleteReview(deletedAt);
    }

    private void validateReviewExists(Long userId, ProgramDate findProgramDate) {
        if (reviewRepository.existsReviewByUser_IdAndProgramDate_Id(userId, findProgramDate.getId())) {
            throw new BadRequestException("이미 작성한 후기입니다.");
        }
    }


    private void validateProgramExists(Long programId) {
        if (!programRepository.existsProgramByIdAndDeletedAtIsNull(programId)) {
            throw new NotFoundException("존재하지 않는 프로그램입니다.");
        }
    }

    private void validateReviewPermission(Long userId, Review findReview) {
        if (!userId.equals(findReview.getUser().getId())) {
            throw new ForbiddenException("해당 후기에 대한 권한이 없습니다.");
        }
    }

    private void validateApplicationCancelled(Application findApplication) {
        if (findApplication.getStatus().equals(ApplicationStatus.CANCLE)) {
            throw new BadRequestException("취소한 신청입니다.");
        }
    }

    private void validateProgramStarted(Application findApplication, LocalDateTime now) {
        // 리뷰한 날짜가 이용 시작일보다 전이면 예외
        if (findApplication.getProgramDate().getDate().isAfter(now)) {
            throw new BadRequestException("아직 프로그램이 시작되지 않았습니다.");
        }
    }

    private Sort getSortByCriteria(String sortBy) {
        return switch (sortBy) {
            case "latest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            case "rating" -> Sort.by(Sort.Direction.DESC, "rating");
            default -> throw new BadRequestException("지원하지 않는 정렬 방식입니다.");
        };
    }
}
