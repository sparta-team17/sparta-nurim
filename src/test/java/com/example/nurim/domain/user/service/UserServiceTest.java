package com.example.nurim.domain.user.service;

import com.example.nurim.domain.application.dto.response.UserApplicationResponse;
import com.example.nurim.domain.application.enums.ApplicationStatus;
import com.example.nurim.domain.application.repository.ApplicationRepository;
import com.example.nurim.domain.common.exception.CustomException;
import com.example.nurim.domain.common.exception.ErrorCode;
import com.example.nurim.domain.review.dto.response.UserReviewResponse;
import com.example.nurim.domain.review.repository.ReviewRepository;
import com.example.nurim.domain.user.dto.request.FindApplicationRequest;
import com.example.nurim.domain.user.dto.request.UpdateNameRequest;
import com.example.nurim.domain.user.dto.request.UpdatePasswordRequest;
import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @InjectMocks
    private UserService userService;

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UpdateNameTests {

        private final UpdateNameRequest request = new UpdateNameRequest("newName");

        @Test
        @Order(1)
        void 이름_수정_사용자_없음_실패() {
            given(userRepository.findActiveByIdOrElseThrow(anyLong()))
                    .willThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

            CustomException thrown = assertThrows(CustomException.class, () -> userService.updateName(1L, request));
            assertEquals(ErrorCode.USER_NOT_FOUND, thrown.getErrorCode());
        }

        @Test
        @Order(2)
        void 이름_수정_성공() {
            User user = new User("temp@gmail.com", "encodedPassword", "name");

            given(userRepository.findActiveByIdOrElseThrow(anyLong())).willReturn(user);

            userService.updateName(1L, request);

            assertEquals(request.getName(), user.getName());
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UpdatePasswordTests {

        private final User user = new User("temp@gmail.com", "encodedPassword", "name");

        @Test
        @Order(1)
        void 비밀번호_수정_사용자_없음_실패() {
            UpdatePasswordRequest request = new UpdatePasswordRequest("oldPassword", "newPassword");

            given(userRepository.findActiveByIdOrElseThrow(anyLong()))
                    .willThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

            CustomException thrown = assertThrows(CustomException.class, () -> userService.updatePassword(1L, request));
            assertEquals(ErrorCode.USER_NOT_FOUND, thrown.getErrorCode());
        }

        @Test
        @Order(2)
        void 비밀번호_수정_현재비밀번호_불일치_실패() {
            UpdatePasswordRequest request = new UpdatePasswordRequest("oldPassword", "newPassword");

            given(userRepository.findActiveByIdOrElseThrow(anyLong())).willReturn(user);
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

            CustomException thrown = assertThrows(CustomException.class, () -> userService.updatePassword(1L, request));
            assertEquals(ErrorCode.PASSWORD_MISMATCH, thrown.getErrorCode());
        }

        @Test
        @Order(3)
        void 비밀번호_수정_새비밀번호_현재비밀번호_일치_실패() {
            UpdatePasswordRequest request = new UpdatePasswordRequest("samePassword", "samePassword");

            given(userRepository.findActiveByIdOrElseThrow(anyLong())).willReturn(user);
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

            CustomException thrown = assertThrows(CustomException.class, () -> userService.updatePassword(1L, request));
            assertEquals(ErrorCode.NEW_PASSWORD_DUPLICATE, thrown.getErrorCode());
        }

        @Test
        @Order(4)
        void 비밀번호_수정_성공() {
            String newEncodedPassword = "newEncodedPassword";
            UpdatePasswordRequest request = new UpdatePasswordRequest("oldPassword", "newPassword");

            given(userRepository.findActiveByIdOrElseThrow(anyLong())).willReturn(user);
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
            given(passwordEncoder.encode(anyString())).willReturn(newEncodedPassword);

            userService.updatePassword(1L, request);

            assertEquals(newEncodedPassword, user.getPassword());
        }
    }

    @Nested
    @Order(3)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class DeleteUserTests {

        private final User user = new User("temp@gmail.com", "encodedPassword", "name");

        @Test
        @Order(1)
        void 회원탈퇴_사용자_없음_실패() {
            given(userRepository.findActiveByIdOrElseThrow(anyLong()))
                    .willThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

            CustomException thrown = assertThrows(CustomException.class, () -> userService.deleteUser(1L));
            assertEquals(ErrorCode.USER_NOT_FOUND, thrown.getErrorCode());
        }

        @Test
        @Order(2)
        void 회원탈퇴_사용하지_않은_신청정보_존재_실패() {
            given(userRepository.findActiveByIdOrElseThrow(anyLong())).willReturn(user);
            given(applicationRepository.existsUnusedApplicationByUserId(anyLong())).willReturn(true);

            CustomException thrown = assertThrows(CustomException.class, () -> userService.deleteUser(1L));
            assertEquals(ErrorCode.UNUSED_APPLICATION_EXISTS, thrown.getErrorCode());
        }

        @Test
        @Order(3)
        void 회원탈퇴_성공() {
            given(userRepository.findActiveByIdOrElseThrow(anyLong())).willReturn(user);
            given(applicationRepository.existsUnusedApplicationByUserId(anyLong())).willReturn(false);

            userService.deleteUser(1L);

            assertNotNull(user.getDeletedAt());
        }
    }

    @Nested
    @Order(4)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class FindReviewsTests {
        @Test
        @Order(1)
        void 사용자_리뷰_조회_성공() {
            List<UserReviewResponse> userReviewResponseList = IntStream.rangeClosed(0, 2)
                    .mapToObj(i -> mock(UserReviewResponse.class))
                    .toList();
            Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
            PageImpl<UserReviewResponse> userReviewResponsePage = new PageImpl<>(userReviewResponseList, pageable, userReviewResponseList.size());

            given(reviewRepository.findActiveByUserId(anyLong(), any(Pageable.class))).willReturn(userReviewResponsePage);

            Page<UserReviewResponse> result = userService.findReviews(1L, 1, 10);

            assertNotNull(result);
            assertEquals(userReviewResponsePage.getTotalElements(), result.getTotalElements());
            assertEquals(userReviewResponsePage.getTotalPages(), result.getTotalPages());
            assertEquals(userReviewResponsePage.getNumber(), result.getNumber());
            assertEquals(userReviewResponsePage.getSize(), result.getSize());
        }
    }

    @Nested
    @Order(5)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class FindApplicationsTests {
        @Test
        @Order(1)
        void 사용자_신청정보_조회_성공() {
            FindApplicationRequest request = mock(FindApplicationRequest.class);

            LocalDateTime createdAt = LocalDateTime.now();
            LocalDateTime usageDateTime = createdAt.plusDays(10);
            List<UserApplicationResponse> userApplicationResponseList = List.of(
                    new UserApplicationResponse(1L, createdAt, ApplicationStatus.COMPLETE, usageDateTime, 1L, "program1", null),
                    new UserApplicationResponse(2L, createdAt, ApplicationStatus.COMPLETE, usageDateTime,2L, "program2", null)
            );
            Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
            Page<UserApplicationResponse> userReviewResponsePage = new PageImpl<>(userApplicationResponseList, pageable, userApplicationResponseList.size());

            given(applicationRepository.findByUserId(anyLong(), any(FindApplicationRequest.class), any(Pageable.class)))
                    .willReturn(userReviewResponsePage);

            Page<UserApplicationResponse> result = userService.findApplications(1L, 1, 10, request);

            assertNotNull(result);
            assertEquals(userReviewResponsePage.getTotalElements(), result.getTotalElements());
            assertEquals(userReviewResponsePage.getTotalPages(), result.getTotalPages());
            assertEquals(userReviewResponsePage.getNumber(), result.getNumber());
            assertEquals(userReviewResponsePage.getSize(), result.getSize());
        }
    }
}