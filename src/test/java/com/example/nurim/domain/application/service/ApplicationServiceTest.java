package com.example.nurim.domain.application.service;

import com.example.nurim.domain.application.dto.response.ApplicationResponseDto;
import com.example.nurim.domain.application.entity.Application;
import com.example.nurim.domain.application.enums.ApplicationStatus;
import com.example.nurim.domain.application.repository.ApplicationRepository;
import com.example.nurim.domain.common.dto.AuthUser;
import com.example.nurim.domain.common.exception.CustomException;
import com.example.nurim.domain.common.exception.ErrorCode;
import com.example.nurim.domain.program.entity.Program;
import com.example.nurim.domain.program.entity.ProgramDate;
import com.example.nurim.domain.program.repository.ProgramDateRepository;
import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private ProgramDateRepository programDateRepository;

    @InjectMocks
    private ApplicationService applicationService;

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CreateApplicationTests {
        private final AuthUser authUser = new AuthUser(1L, "test@gmail.com", "name", Collections.emptyList());
        private final Long programDateId = 100L;

        private User user;
        private Program program;
        private ProgramDate programDate;

        @BeforeEach
        void setUp() {
            // User 초기화 및 id 설정
            user = new User("test@gmail.com", "encodedPassword", "name");
            try {
                Field userIdField = User.class.getDeclaredField("id");
                userIdField.setAccessible(true);
                userIdField.set(user, 1L);
            } catch (Exception e) {
                fail("User 초기화 실패: " + e.getMessage());
            }

            program = new Program();
            try {
                Field regEndField = Program.class.getDeclaredField("registrationEndDate");
                regEndField.setAccessible(true);
                // 프로그램 등록 마감일을 미래로 설정
                regEndField.set(program, LocalDateTime.now().plusDays(2));

                Field quotaField = Program.class.getDeclaredField("quota");
                quotaField.setAccessible(true);
                quotaField.set(program, 10L);
            } catch (Exception e) {
                fail("Program 초기화 실패: " + e.getMessage());
            }

            // programDate 초기화 (신청 가능한 날짜)
            programDate = new ProgramDate(program, LocalDateTime.now().plusDays(1));
        }

        @Test
        @Order(1)
        void 사용자_없으면_실패() {
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            CustomException thrown = assertThrows(CustomException.class, () ->
                    applicationService.createApplication(authUser, programDateId));
            assertEquals(ErrorCode.USER_NOT_FOUND, thrown.getErrorCode());
        }

        @Test
        @Order(2)
        void 프로그램일정_없으면_실패() {
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
            given(programDateRepository.findById(anyLong())).willReturn(Optional.empty());

            CustomException thrown = assertThrows(CustomException.class, () ->
                    applicationService.createApplication(authUser, programDateId));
            assertEquals(ErrorCode.PROGRAM_DATE_NOT_FOUND, thrown.getErrorCode());
        }

        @Test
        @Order(3)
        void 신청_마감된_프로그램이면_실패() {
            // 신청 마감 조건: 프로그램 일정 날짜가 과거이면 신청 불가
            ProgramDate pastProgramDate = new ProgramDate(program, LocalDateTime.now().minusDays(1));

            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
            // 과거 날짜를 반환하도록 stubbing
            given(programDateRepository.findById(anyLong())).willReturn(Optional.of(pastProgramDate));

            CustomException thrown = assertThrows(CustomException.class, () ->
                    applicationService.createApplication(authUser, programDateId));
            assertEquals(ErrorCode.PROGRAM_DATE_CLOSED, thrown.getErrorCode());
        }

        @Test
        @Order(4)
        void 중복_신청이면_실패() {
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
            given(programDateRepository.findById(anyLong())).willReturn(Optional.of(programDate));
            given(applicationRepository.existsByProgramDateIdAndUserId(anyLong(), anyLong())).willReturn(true);

            CustomException thrown = assertThrows(CustomException.class, () ->
                    applicationService.createApplication(authUser, programDateId));
            assertEquals(ErrorCode.DUPLICATE_APPLICATION, thrown.getErrorCode());
        }

        @Test
        @Order(5)
        void 선착순_신청_마감이면_실패() {
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
            given(programDateRepository.findById(anyLong())).willReturn(Optional.of(programDate));
            given(applicationRepository.existsByProgramDateIdAndUserId(anyLong(), anyLong())).willReturn(false);
            // 신청된 수가 quota와 같으면 선착순 신청 마감 처리
            given(applicationRepository.countByProgramDateId(anyLong())).willReturn((long) getProgramQuota(program));

            CustomException thrown = assertThrows(CustomException.class, () ->
                    applicationService.createApplication(authUser, programDateId));
            assertEquals(ErrorCode.APPLICATION_FULL, thrown.getErrorCode());
        }

        @Test
        @Order(6)
        void 정상_신청_성공() {
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
            given(programDateRepository.findById(anyLong())).willReturn(Optional.of(programDate));
            given(applicationRepository.existsByProgramDateIdAndUserId(anyLong(), anyLong())).willReturn(false);
            given(applicationRepository.countByProgramDateId(anyLong())).willReturn(0L);

            // 저장 시 입력값 그대로 반환하도록 설정
            when(applicationRepository.save(any(Application.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(programDateRepository.save(any(ProgramDate.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            ApplicationResponseDto response = applicationService.createApplication(authUser, programDateId);

            assertNotNull(response);
            verify(applicationRepository, times(1)).save(any(Application.class));
            verify(programDateRepository, times(1)).save(any(ProgramDate.class));
        }

        private int getProgramQuota(Program program) {
            try {
                Field quotaField = Program.class.getDeclaredField("quota");
                quotaField.setAccessible(true);
                return ((Long) quotaField.get(program)).intValue();
            } catch (Exception e) {
                fail("quota 가져오기 실패: " + e.getMessage());
                return 0;
            }
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CancelApplicationTests {
        private final AuthUser authUser = new AuthUser(1L, "test@gmail.com", "name", Collections.emptyList());
        private final Long programDateId = 100L;
        private final Long applicationId = 200L;

        private User user;
        private Program program;
        private ProgramDate programDate;
        private Application application;

        @BeforeEach
        void setUp() {
            user = new User("test@gmail.com", "encodedPassword", "name");
            try {
                Field userIdField = User.class.getDeclaredField("id");
                userIdField.setAccessible(true);
                userIdField.set(user, 1L);
            } catch (Exception e) {
                fail("User 초기화 실패: " + e.getMessage());
            }

            program = new Program();
            try {
                Field regEndField = Program.class.getDeclaredField("registrationEndDate");
                regEndField.setAccessible(true);
                // 기본적으로 취소 가능한 기간으로 설정
                regEndField.set(program, LocalDateTime.now().plusDays(2));

                Field quotaField = Program.class.getDeclaredField("quota");
                quotaField.setAccessible(true);
                quotaField.set(program, 10L);
            } catch (Exception e) {
                fail("Program 초기화 실패: " + e.getMessage());
            }
            programDate = new ProgramDate(program, LocalDateTime.now().plusDays(1));
            application = new Application(programDate, user, ApplicationStatus.COMPLETE, LocalDateTime.now());
        }

        @Test
        @Order(1)
        void 사용자_없으면_취소_실패() {
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            CustomException thrown = assertThrows(CustomException.class, () ->
                    applicationService.cancelApplication(authUser, programDateId, applicationId));
            assertEquals(ErrorCode.USER_NOT_FOUND, thrown.getErrorCode());
        }

        @Test
        @Order(2)
        void 신청_없으면_취소_실패() {
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
            given(applicationRepository.findById(anyLong())).willReturn(Optional.empty());

            CustomException thrown = assertThrows(CustomException.class, () ->
                    applicationService.cancelApplication(authUser, programDateId, applicationId));
            assertEquals(ErrorCode.APPLICATION_NOT_FOUND, thrown.getErrorCode());
        }

        @Test
        @Order(3)
        void 신청_소유자_아니면_취소_실패() {
            // 다른 사용자가 신청한 경우
            User otherUser = new User("other@gmail.com", "password", "other");
            // 다른 사용자의 id 설정
            try {
                Field idField = User.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(otherUser, 2L);
            } catch (Exception e) {
                fail("otherUser 초기화 실패: " + e.getMessage());
            }
            Application otherApplication = new Application(
                    programDate,
                    otherUser,
                    ApplicationStatus.COMPLETE,
                    LocalDateTime.now()
            );

            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
            given(applicationRepository.findById(anyLong())).willReturn(Optional.of(otherApplication));

            CustomException thrown = assertThrows(CustomException.class, () ->
                    applicationService.cancelApplication(authUser, programDateId, applicationId));
            assertEquals(ErrorCode.APPLICATION_NOT_OWNER, thrown.getErrorCode());
        }

        @Test
        @Order(4)
        void 취소_기간_만료이면_실패() {
            // 취소 가능 기간이 지난 상황을 위해 Program의 registrationEndDate를 과거로 수정
            try {
                Field regEndField = Program.class.getDeclaredField("registrationEndDate");
                regEndField.setAccessible(true);
                regEndField.set(program, LocalDateTime.now().minusDays(1));
            } catch (Exception e) {
                fail("registrationEndDate 수정 실패: " + e.getMessage());
            }

            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
            given(applicationRepository.findById(anyLong())).willReturn(Optional.of(application));

            CustomException thrown = assertThrows(CustomException.class, () ->
                    applicationService.cancelApplication(authUser, programDateId, applicationId));
            assertEquals(ErrorCode.CANCELLATION_PERIOD_EXPIRED, thrown.getErrorCode());
        }

        @Test
        @Order(5)
        void 정상_취소_성공() {
            // 정상적인 취소 케이스 (취소 기간 내)
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
            given(applicationRepository.findById(anyLong())).willReturn(Optional.of(application));

            when(applicationRepository.save(any(Application.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(programDateRepository.save(any(ProgramDate.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // 기본적으로 등록 마감일이 미래이므로 취소 가능
            applicationService.cancelApplication(authUser, programDateId, applicationId);

            verify(applicationRepository, times(1)).save(any(Application.class));
            verify(programDateRepository, times(1)).save(any(ProgramDate.class));
        }
    }
}
