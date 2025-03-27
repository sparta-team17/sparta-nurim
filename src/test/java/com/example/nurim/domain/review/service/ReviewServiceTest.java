package com.example.nurim.domain.review.service;

import com.example.nurim.domain.application.entity.Application;
import com.example.nurim.domain.application.enums.ApplicationStatus;
import com.example.nurim.domain.application.repository.ApplicationRepository;
import com.example.nurim.domain.common.exception.CustomException;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ProgramRepository programRepository;
    @Mock
    private ProgramDateRepository programDateRepository;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @InjectMocks
    private ReviewService reviewService;

    @Nested
    class CreateReviewTest {
        @Test
        void 리뷰_생성_성공() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            Long programDateId = 1L;
            Long applicationId = 1L;
            Long reviewId = 1L;
            double rating = 5.0;
            String email = "abc@test.com";
            String password = "1234";
            String name = "이름";
            String contents = "리뷰내용";
            ApplicationStatus status = ApplicationStatus.COMPLETE;
            LocalDateTime date = LocalDateTime.of(2025, 3, 25, 9, 30);

            ReviewSaveRequestDto requestDto = new ReviewSaveRequestDto(rating, contents);

            User user = new User(email, password, name);
            ReflectionTestUtils.setField(user, "id", userId);

            Program program = new Program();
            ReflectionTestUtils.setField(program, "id", programId);

            ProgramDate programDate = new ProgramDate();
            ReflectionTestUtils.setField(programDate, "id", programDateId);
            ReflectionTestUtils.setField(programDate, "program", program);
            ReflectionTestUtils.setField(programDate, "date", date);

            Application application = new Application();
            ReflectionTestUtils.setField(application, "id", applicationId);
            ReflectionTestUtils.setField(application, "user", user);
            ReflectionTestUtils.setField(application, "programDate", programDate);
            ReflectionTestUtils.setField(application, "status", status);

            Review review = new Review();
            ReflectionTestUtils.setField(review, "id", reviewId);
            ReflectionTestUtils.setField(review, "rating", rating);
            ReflectionTestUtils.setField(review, "contents", contents);
            ReflectionTestUtils.setField(review, "user", user);
            ReflectionTestUtils.setField(review, "programDate", programDate);

            given(applicationRepository.findById(anyLong())).willReturn(Optional.of(application));
            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(true);
            given(programDateRepository.findById(anyLong())).willReturn(Optional.of(programDate));
            given(reviewRepository.existsReviewByUserIdAndProgramDateId(anyLong(), anyLong())).willReturn(false);
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
            given(reviewRepository.save(any())).willReturn(review);

            // When
            ReviewSaveResponseDto responseDto = reviewService.createReview(userId, applicationId, requestDto);

            // Then
            assertThat(responseDto).isNotNull();
            assertThat(responseDto.getContents()).isEqualTo(contents);
        }

        @Test
        void 리뷰_생성_신청_조회_실패() {
            // Given
            Long userId = 1L;
            Long applicationId = 1L;
            double rating = 5.0;
            String contents = "리뷰내용";

            ReviewSaveRequestDto requestDto = new ReviewSaveRequestDto(rating, contents);

            given(applicationRepository.findById(anyLong())).willReturn(Optional.empty());

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.createReview(userId, applicationId, requestDto));
            assertThat(HttpStatus.NOT_FOUND).isEqualTo(thrown.getStatus());
            assertThat("존재하지 않는 신청입니다.").isEqualTo(thrown.getMessage());
        }

        @Test
        void 리뷰_생성_프로그램_조회_실패() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            Long programDateId = 1L;
            Long applicationId = 1L;
            double rating = 5.0;
            String contents = "리뷰내용";

            ReviewSaveRequestDto requestDto = new ReviewSaveRequestDto(rating, contents);

            Program program = new Program();
            ReflectionTestUtils.setField(program, "id", programId);

            ProgramDate programDate = new ProgramDate();
            ReflectionTestUtils.setField(programDate, "id", programDateId);
            ReflectionTestUtils.setField(programDate, "program", program);

            Application application = new Application();
            ReflectionTestUtils.setField(application, "id", applicationId);
            ReflectionTestUtils.setField(application, "programDate", programDate);

            given(applicationRepository.findById(anyLong())).willReturn(Optional.of(application));
            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(false);

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.createReview(userId, applicationId, requestDto));
            assertThat(HttpStatus.NOT_FOUND).isEqualTo(thrown.getStatus());
            assertThat("존재하지 않는 프로그램입니다.").isEqualTo(thrown.getMessage());
        }

        @Test
        void 리뷰_생성_취소된_신청() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            Long programDateId = 1L;
            Long applicationId = 1L;
            double rating = 5.0;
            String contents = "리뷰내용";
            ApplicationStatus status = ApplicationStatus.CANCEL;

            ReviewSaveRequestDto requestDto = new ReviewSaveRequestDto(rating, contents);

            Program program = new Program();
            ReflectionTestUtils.setField(program, "id", programId);

            ProgramDate programDate = new ProgramDate();
            ReflectionTestUtils.setField(programDate, "id", programDateId);
            ReflectionTestUtils.setField(programDate, "program", program);

            Application application = new Application();
            ReflectionTestUtils.setField(application, "id", applicationId);
            ReflectionTestUtils.setField(application, "programDate", programDate);
            ReflectionTestUtils.setField(application, "status", status);

            given(applicationRepository.findById(anyLong())).willReturn(Optional.of(application));
            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(true);

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.createReview(userId, applicationId, requestDto));
            assertThat(HttpStatus.BAD_REQUEST).isEqualTo(thrown.getStatus());
            assertThat("취소한 신청입니다.").isEqualTo(thrown.getMessage());
        }

        @Test
        void 리뷰_생성_리뷰한_날짜가_일정보다_전이므로_실패() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            Long programDateId = 1L;
            Long applicationId = 1L;
            double rating = 5.0;
            String contents = "리뷰내용";
            ApplicationStatus status = ApplicationStatus.COMPLETE;
            LocalDateTime date = LocalDateTime.of(2025, 3, 28, 9, 30);

            ReviewSaveRequestDto requestDto = new ReviewSaveRequestDto(rating, contents);

            Program program = new Program();
            ReflectionTestUtils.setField(program, "id", programId);

            ProgramDate programDate = new ProgramDate();
            ReflectionTestUtils.setField(programDate, "id", programDateId);
            ReflectionTestUtils.setField(programDate, "program", program);
            ReflectionTestUtils.setField(programDate, "date", date);

            Application application = new Application();
            ReflectionTestUtils.setField(application, "id", applicationId);
            ReflectionTestUtils.setField(application, "programDate", programDate);
            ReflectionTestUtils.setField(application, "status", status);

            given(applicationRepository.findById(anyLong())).willReturn(Optional.of(application));
            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(true);

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.createReview(userId, applicationId, requestDto));
            assertThat(HttpStatus.BAD_REQUEST).isEqualTo(thrown.getStatus());
            assertThat("아직 프로그램이 시작되지 않았습니다.").isEqualTo(thrown.getMessage());
        }

        @Test
        void 리뷰_생성_프로그램_일정_조회_실패() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            Long programDateId = 1L;
            Long applicationId = 1L;
            double rating = 5.0;
            String contents = "리뷰내용";
            ApplicationStatus status = ApplicationStatus.COMPLETE;
            LocalDateTime date = LocalDateTime.of(2025, 3, 26, 9, 30);

            ReviewSaveRequestDto requestDto = new ReviewSaveRequestDto(rating, contents);

            Program program = new Program();
            ReflectionTestUtils.setField(program, "id", programId);

            ProgramDate programDate = new ProgramDate();
            ReflectionTestUtils.setField(programDate, "id", programDateId);
            ReflectionTestUtils.setField(programDate, "program", program);
            ReflectionTestUtils.setField(programDate, "date", date);

            Application application = new Application();
            ReflectionTestUtils.setField(application, "id", applicationId);
            ReflectionTestUtils.setField(application, "programDate", programDate);
            ReflectionTestUtils.setField(application, "status", status);

            given(applicationRepository.findById(anyLong())).willReturn(Optional.of(application));
            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(true);
            given(programDateRepository.findById(anyLong())).willReturn(Optional.empty());

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.createReview(userId, applicationId, requestDto));
            assertThat(HttpStatus.NOT_FOUND).isEqualTo(thrown.getStatus());
            assertThat("존재하지 않는 프로그램 일정입니다.").isEqualTo(thrown.getMessage());
        }

        @Test
        void 리뷰_생성_이미_작성한_리뷰이므로_실패() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            Long programDateId = 1L;
            Long applicationId = 1L;
            double rating = 5.0;
            String contents = "리뷰내용";
            ApplicationStatus status = ApplicationStatus.COMPLETE;
            LocalDateTime date = LocalDateTime.of(2025, 3, 26, 9, 30);

            ReviewSaveRequestDto requestDto = new ReviewSaveRequestDto(rating, contents);

            Program program = new Program();
            ReflectionTestUtils.setField(program, "id", programId);

            ProgramDate programDate = new ProgramDate();
            ReflectionTestUtils.setField(programDate, "id", programDateId);
            ReflectionTestUtils.setField(programDate, "program", program);
            ReflectionTestUtils.setField(programDate, "date", date);

            Application application = new Application();
            ReflectionTestUtils.setField(application, "id", applicationId);
            ReflectionTestUtils.setField(application, "programDate", programDate);
            ReflectionTestUtils.setField(application, "status", status);

            given(applicationRepository.findById(anyLong())).willReturn(Optional.of(application));
            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(true);
            given(programDateRepository.findById(anyLong())).willReturn(Optional.of(programDate));
            given(reviewRepository.existsReviewByUserIdAndProgramDateId(anyLong(), anyLong())).willReturn(true);

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.createReview(userId, applicationId, requestDto));
            assertThat(HttpStatus.BAD_REQUEST).isEqualTo(thrown.getStatus());
            assertThat("이미 작성한 후기입니다.").isEqualTo(thrown.getMessage());
        }

        @Test
        void 리뷰_생성_유저_조회_실패() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            Long programDateId = 1L;
            Long applicationId = 1L;
            double rating = 5.0;
            String contents = "리뷰내용";
            ApplicationStatus status = ApplicationStatus.COMPLETE;
            LocalDateTime date = LocalDateTime.of(2025, 3, 26, 9, 30);

            ReviewSaveRequestDto requestDto = new ReviewSaveRequestDto(rating, contents);

            Program program = new Program();
            ReflectionTestUtils.setField(program, "id", programId);

            ProgramDate programDate = new ProgramDate();
            ReflectionTestUtils.setField(programDate, "id", programDateId);
            ReflectionTestUtils.setField(programDate, "program", program);
            ReflectionTestUtils.setField(programDate, "date", date);

            Application application = new Application();
            ReflectionTestUtils.setField(application, "id", applicationId);
            ReflectionTestUtils.setField(application, "programDate", programDate);
            ReflectionTestUtils.setField(application, "status", status);

            given(applicationRepository.findById(anyLong())).willReturn(Optional.of(application));
            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(true);
            given(programDateRepository.findById(anyLong())).willReturn(Optional.of(programDate));
            given(reviewRepository.existsReviewByUserIdAndProgramDateId(anyLong(), anyLong())).willReturn(false);
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.createReview(userId, applicationId, requestDto));
            assertThat(HttpStatus.NOT_FOUND).isEqualTo(thrown.getStatus());
            assertThat("존재하지 않는 유저입니다.").isEqualTo(thrown.getMessage());
        }
    }

    @Nested
    class FindAllReviewTest {
        @Test
        void 리뷰_전체_조회_최신순_정렬_성공() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            int page = 1;
            int size = 10;
            String email = "abc@test.com";
            String password = "1234";
            String name = "이름";
            String sortBy = "latest";
            LocalDateTime createdAt1 = LocalDateTime.of(2025, 3, 26, 9, 30);
            LocalDateTime createdAt2 = LocalDateTime.of(2025, 3, 25, 9, 30);

            User user = new User(email, password, name);
            ReflectionTestUtils.setField(user, "id", userId);

            Program program = new Program();
            ReflectionTestUtils.setField(program, "id", programId);

            ProgramDate programDate1 = new ProgramDate();
            ReflectionTestUtils.setField(programDate1, "id", 1L);
            ReflectionTestUtils.setField(programDate1, "program", program);

            ProgramDate programDate2 = new ProgramDate();
            ReflectionTestUtils.setField(programDate2, "id", 2L);
            ReflectionTestUtils.setField(programDate2, "program", program);

            Review review1 = new Review();
            ReflectionTestUtils.setField(review1, "id", 1L);
            ReflectionTestUtils.setField(review1, "rating", 5.0);
            ReflectionTestUtils.setField(review1, "contents", "리뷰내용1");
            ReflectionTestUtils.setField(review1, "user", user);
            ReflectionTestUtils.setField(review1, "programDate", programDate1);
            ReflectionTestUtils.setField(review1, "createdAt", createdAt1);

            Review review2 = new Review();
            ReflectionTestUtils.setField(review2, "id", 2L);
            ReflectionTestUtils.setField(review2, "rating", 4.0);
            ReflectionTestUtils.setField(review2, "contents", "리뷰내용2");
            ReflectionTestUtils.setField(review2, "user", user);
            ReflectionTestUtils.setField(review2, "programDate", programDate2);
            ReflectionTestUtils.setField(review2, "createdAt", createdAt2);

            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

            List<Review> reviewList = List.of(review1, review2);

            Page<Review> mockPage = new PageImpl<>(reviewList, pageable, reviewList.size());

            given(programRepository.findProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.of(program));
            given(reviewRepository.findReviewsByProgramId(anyLong(), any())).willReturn(mockPage);

            // When
            Page<ReviewResponseDto> response = reviewService.findAllReview(programId, page, size, sortBy);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getContent()).hasSize(2);
            assertThat(response.getContent().get(0).getContents()).isEqualTo("리뷰내용1");
            assertThat(response.getTotalElements()).isEqualTo(2);
        }

        @Test
        void 리뷰_전체_조회_별점순_정렬_성공() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            int page = 1;
            int size = 10;
            String email = "abc@test.com";
            String password = "1234";
            String name = "이름";
            String sortBy = "rating";

            User user = new User(email, password, name);
            ReflectionTestUtils.setField(user, "id", userId);

            Program program = new Program();
            ReflectionTestUtils.setField(program, "id", programId);

            ProgramDate programDate1 = new ProgramDate();
            ReflectionTestUtils.setField(programDate1, "id", 1L);
            ReflectionTestUtils.setField(programDate1, "program", program);

            ProgramDate programDate2 = new ProgramDate();
            ReflectionTestUtils.setField(programDate2, "id", 2L);
            ReflectionTestUtils.setField(programDate2, "program", program);

            Review review1 = new Review();
            ReflectionTestUtils.setField(review1, "id", 1L);
            ReflectionTestUtils.setField(review1, "rating", 5.0);
            ReflectionTestUtils.setField(review1, "contents", "리뷰내용1");
            ReflectionTestUtils.setField(review1, "user", user);
            ReflectionTestUtils.setField(review1, "programDate", programDate1);

            Review review2 = new Review();
            ReflectionTestUtils.setField(review2, "id", 2L);
            ReflectionTestUtils.setField(review2, "rating", 4.0);
            ReflectionTestUtils.setField(review2, "contents", "리뷰내용2");
            ReflectionTestUtils.setField(review2, "user", user);
            ReflectionTestUtils.setField(review2, "programDate", programDate2);

            Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, sortBy));

            List<Review> reviewList = List.of(review1, review2);

            Page<Review> mockPage = new PageImpl<>(reviewList, pageable, reviewList.size());

            given(programRepository.findProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.of(program));
            given(reviewRepository.findReviewsByProgramId(anyLong(), any())).willReturn(mockPage);

            // When
            Page<ReviewResponseDto> response = reviewService.findAllReview(programId, page, size, sortBy);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getContent()).hasSize(2);
            assertThat(response.getContent().get(0).getContents()).isEqualTo("리뷰내용1");
            assertThat(response.getTotalElements()).isEqualTo(2);
        }

        @Test
        void 리뷰_전체_조회_프로그램_조회_실패() {
            // Given
            Long programId = 1L;
            int page = 1;
            int size = 10;
            String sortBy = "none";

            Program program = new Program();
            ReflectionTestUtils.setField(program, "id", programId);

            given(programRepository.findProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.of(program));

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.findAllReview(programId, page, size, sortBy));
            assertThat(HttpStatus.BAD_REQUEST).isEqualTo(thrown.getStatus());
            assertThat("지원하지 않는 정렬 방식입니다.").isEqualTo(thrown.getMessage());
        }

        @Test
        void 리뷰_전체_조회_올바르지_않은_정렬값_실패() {
            // Given
            Long programId = 1L;
            int page = 1;
            int size = 10;
            String sortBy = "rating";

            given(programRepository.findProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.empty());

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.findAllReview(programId, page, size, sortBy));
            assertThat(HttpStatus.NOT_FOUND).isEqualTo(thrown.getStatus());
            assertThat("존재하지 않는 프로그램입니다.").isEqualTo(thrown.getMessage());
        }
    }

    @Nested
    class FindOneReviewTest {
        @Test
        void 리뷰_단건_조회_성공() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            Long reviewId = 1L;
            String email = "abc@test.com";
            String password = "1234";
            String name = "이름";
            String contents = "리뷰내용";

            User user = new User(email, password, name);
            ReflectionTestUtils.setField(user, "id", userId);

            Program program = new Program();
            ReflectionTestUtils.setField(program, "id", programId);

            ProgramDate programDate = new ProgramDate();
            ReflectionTestUtils.setField(programDate, "id", 1L);

            Review review = new Review();
            ReflectionTestUtils.setField(review, "id", 1L);
            ReflectionTestUtils.setField(review, "rating", 5.0);
            ReflectionTestUtils.setField(review, "contents", contents);
            ReflectionTestUtils.setField(review, "user", user);
            ReflectionTestUtils.setField(review, "programDate", programDate);

            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(true);
            given(reviewRepository.findReviewByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.of(review));

            // When
            ReviewResponseDto responseDto = reviewService.findOneReview(programId, reviewId);

            // Then
            assertThat(responseDto).isNotNull();
            assertThat(responseDto.getContents()).isEqualTo(contents);
        }

        @Test
        void 리뷰_단건_조회_프로그램_조회_실패() {
            // Given
            Long programId = 1L;
            Long reviewId = 1L;

            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(false);

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.findOneReview(programId, reviewId));
            assertThat(HttpStatus.NOT_FOUND).isEqualTo(thrown.getStatus());
            assertThat("존재하지 않는 프로그램입니다.").isEqualTo(thrown.getMessage());
        }

        @Test
        void 리뷰_단건_조회_리뷰_조회_실패() {
            // Given
            Long programId = 1L;
            Long reviewId = 1L;

            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(true);
            given(reviewRepository.findReviewByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.empty());

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.findOneReview(programId, reviewId));
            assertThat(HttpStatus.NOT_FOUND).isEqualTo(thrown.getStatus());
            assertThat("존재하지 않는 후기입니다.").isEqualTo(thrown.getMessage());
        }
    }

    @Nested
    class UpdateReviewTest {
        @Test
        void 리뷰_수정_성공() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            Long reviewId = 1L;
            double rating = 5.0;
            String email = "abc@test.com";
            String password = "1234";
            String name = "이름";
            String contents = "리뷰내용수정";

            ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto(rating, contents);

            User user = new User(email, password, name);
            ReflectionTestUtils.setField(user, "id", userId);

            Program program = new Program();
            ReflectionTestUtils.setField(program, "id", programId);

            ProgramDate programDate = new ProgramDate();
            ReflectionTestUtils.setField(programDate, "id", 1L);

            Review findReview = new Review();
            ReflectionTestUtils.setField(findReview, "id", 1L);
            ReflectionTestUtils.setField(findReview, "rating", 4.0);
            ReflectionTestUtils.setField(findReview, "contents", "리뷰내용");
            ReflectionTestUtils.setField(findReview, "user", user);

            Review updateReview = new Review();
            ReflectionTestUtils.setField(updateReview, "id", 1L);
            ReflectionTestUtils.setField(updateReview, "rating", rating);
            ReflectionTestUtils.setField(updateReview, "contents", contents);
            ReflectionTestUtils.setField(updateReview, "user", user);

            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(true);
            given(reviewRepository.findReviewByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.of(findReview));
            given(reviewRepository.save(any())).willReturn(updateReview);

            // When
            ReviewUpdateResponseDto responseDto = reviewService.updateReview(userId, programId, reviewId, requestDto);

            // Then
            assertThat(responseDto).isNotNull();
            assertThat(responseDto.getContents()).isEqualTo(contents);
        }

        @Test
        void 리뷰_수정_프로그램_조회_실패() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            Long reviewId = 1L;
            double rating = 5.0;
            String contents = "리뷰내용";

            ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto(rating, contents);

            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(false);

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.updateReview(userId, programId, reviewId, requestDto));
            assertThat(HttpStatus.NOT_FOUND).isEqualTo(thrown.getStatus());
            assertThat("존재하지 않는 프로그램입니다.").isEqualTo(thrown.getMessage());
        }

        @Test
        void 리뷰_수정_리뷰_조회_실패() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            Long reviewId = 1L;
            double rating = 5.0;
            String contents = "리뷰내용";

            ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto(rating, contents);

            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(true);
            given(reviewRepository.findReviewByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.empty());

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.updateReview(userId, programId, reviewId, requestDto));
            assertThat(HttpStatus.NOT_FOUND).isEqualTo(thrown.getStatus());
            assertThat("존재하지 않는 후기입니다.").isEqualTo(thrown.getMessage());
        }

        @Test
        void 리뷰_수정_본인이_작성한_리뷰가_아니므로_실패() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            Long reviewId = 1L;
            double rating = 5.0;
            String email = "abc@test.com";
            String password = "1234";
            String name = "이름";
            String contents = "리뷰내용";

            ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto(rating, contents);

            User user = new User(email, password, name);
            ReflectionTestUtils.setField(user, "id", 2L);

            Review review = new Review();
            ReflectionTestUtils.setField(review, "id", 1L);
            ReflectionTestUtils.setField(review, "user", user);

            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(true);
            given(reviewRepository.findReviewByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.of(review));

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.updateReview(userId, programId, reviewId, requestDto));
            assertThat(HttpStatus.FORBIDDEN).isEqualTo(thrown.getStatus());
            assertThat("해당 후기에 대한 권한이 없습니다.").isEqualTo(thrown.getMessage());
        }
    }

    @Nested
    class DeleteReviewTest {
        @Test
        void 리뷰_삭제_성공() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            Long reviewId = 1L;
            String email = "abc@test.com";
            String password = "1234";
            String name = "이름";

            User user = new User(email, password, name);
            ReflectionTestUtils.setField(user, "id", userId);

            Review review = new Review();
            ReflectionTestUtils.setField(review, "id", 1L);
            ReflectionTestUtils.setField(review, "user", user);

            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(true);
            given(reviewRepository.findReviewByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.of(review));

            // When
            reviewService.deleteReview(userId, programId, reviewId);

            // Then
            assertThat(review.getDeletedAt()).isNotNull();
        }

        @Test
        void 리뷰_삭제_프로그램_조회_실패() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            Long reviewId = 1L;

            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(false);

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.deleteReview(userId, programId, reviewId));
            assertThat(HttpStatus.NOT_FOUND).isEqualTo(thrown.getStatus());
            assertThat("존재하지 않는 프로그램입니다.").isEqualTo(thrown.getMessage());
        }

        @Test
        void 리뷰_삭제_리뷰_조회_실패() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            Long reviewId = 1L;

            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(true);
            given(reviewRepository.findReviewByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.empty());

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.deleteReview(userId, programId, reviewId));
            assertThat(HttpStatus.NOT_FOUND).isEqualTo(thrown.getStatus());
            assertThat("존재하지 않는 후기입니다.").isEqualTo(thrown.getMessage());
        }

        @Test
        void 리뷰_삭제_본인이_작성한_리뷰가_아니므로_실패() {
            // Given
            Long userId = 1L;
            Long programId = 1L;
            Long reviewId = 1L;
            String email = "abc@test.com";
            String password = "1234";
            String name = "이름";

            User user = new User(email, password, name);
            ReflectionTestUtils.setField(user, "id", 2L);

            Review review = new Review();
            ReflectionTestUtils.setField(review, "id", 1L);
            ReflectionTestUtils.setField(review, "user", user);

            given(programRepository.existsProgramByIdAndDeletedAtIsNull(anyLong())).willReturn(true);
            given(reviewRepository.findReviewByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.of(review));

            // When, Then
            CustomException thrown = assertThrows(CustomException.class, () -> reviewService.deleteReview(userId, programId, reviewId));
            assertThat(HttpStatus.FORBIDDEN).isEqualTo(thrown.getStatus());
            assertThat("해당 후기에 대한 권한이 없습니다.").isEqualTo(thrown.getMessage());
        }
    }
}