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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final ProgramDateRepository programDateRepository;

    @Transactional
    public ApplicationResponseDto createApplication(Long userId, Long programDateId) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 프로그램 일정 조회
        ProgramDate programDate = programDateRepository.findById(programDateId)
                .orElseThrow(()-> new CustomException(ErrorCode.PROGRAM_DATE_NOT_FOUND));

        // 프로그램 신청 가능 여부 확인 (접수 종료일 기준)
        Program program = programDate.getProgram();
        LocalDateTime now = LocalDateTime.now();

        if (program.getRegistrationStartDate().isAfter(now)) {
            throw new CustomException(ErrorCode.PROGRAM_DATE_NOT_OPENED);
        }

        if (program.getRegistrationEndDate().isBefore(now)) {
            throw new CustomException(ErrorCode.PROGRAM_DATE_CLOSED); // 접수 종료 후 신청 불가
        }

        // 중복 신청 방지
        if (applicationRepository.existsByProgramDateIdAndUserId(programDateId, user.getId())) {
            throw new CustomException(ErrorCode.DUPLICATE_APPLICATION);
        }

        // 선착순 신청 확인 (최대 인원 인원 수)
        long currentApplicationsCount = programDate.getCount();
        if (currentApplicationsCount >= program.getQuota()) {
            throw new CustomException(ErrorCode.APPLICATION_FULL);
        }

        // 신청 생성
        Application application = Application.builder()
                        .user(user)
                        .programDate(programDate)
                        .status(ApplicationStatus.COMPLETE)
                        .useDate(programDate.getDate())
                        .isReviewd(false)
                        .build();

        applicationRepository.save(application);

        // 신청 완료 후 ProgramDate.count 증가
        programDate.incrementCount();
        programDateRepository.save(programDate);

        return new ApplicationResponseDto(application);
    }

    @Transactional
    public void deleteApplication(AuthUser authUser, Long programDateId, Long applicationId) {
        // 사용자 조회
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 신청 조회 (본인 신청인지 확인)
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(()-> new CustomException(ErrorCode.APPLICATION_NOT_FOUND));

        if (!application.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.APPLICATION_NOT_OWNER);
        }

        // 신청 취소 가능 여부 확인 (접수 종료일 기준 1일 전까지 취소 가능)
        if (application.getProgramDate().getProgram().getRegistrationEndDate().minusDays(1).isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.CANCELLATION_PERIOD_EXPIRED);
        }

        // 신청 상태 변경
        application.cancelApplication();

        // 신청 취소 시 ProgramDate.count 감소
        application.getProgramDate().decrementCount();

        // 변경 사항 저장
        applicationRepository.save(application);
        programDateRepository.save(application.getProgramDate());
    }
}
