package com.example.nurim.domain.application.service;

import com.example.nurim.domain.application.dto.request.ApplicationRequestDto;
import com.example.nurim.domain.application.dto.response.ApplicationResponseDto;
import com.example.nurim.domain.application.entity.Application;
import com.example.nurim.domain.application.exception.ApplicationException;
import com.example.nurim.domain.application.repository.ApplicationRepository;
import com.example.nurim.domain.common.dto.AuthUser;
import com.example.nurim.domain.program.entity.ProgramDate;
import com.example.nurim.domain.program.repository.ProgramDateRepository;
import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final ProgramDateRepository programDateRepository;

    @Transactional
    public ApplicationResponseDto createApplication(AuthUser authUser, Long programDateId, ApplicationRequestDto requestDto) {
        // 사용자 조회
        User user = userRepository.findActiveByIdOrElseThrow(authUser.getId());

        // 프로그램 일정 조회
        ProgramDate programDate = programDateRepository.findById(programDateId)
                .orElseThrow(()-> new ApplicationException(HttpStatus.NOT_FOUND, "Program date not found"));

        // 중복 신청 방지
        if (applicationRepository.existsByProgramDateIdAndUserId(programDateId, user.getId())) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Application already exists");
        }

        // 신청 생성
        Application application = Application.builder()
                        .user(user)
                        .programDate(programDate)
                        .build();

        return new ApplicationResponseDto(application);
    }

    @Transactional
    public void deleteApplication(AuthUser authUser, Long programDateId, Long applicationId) {
        // 사용자 조회
        User user = userRepository.findActiveByIdOrELseThrow(authUser.getId());

        // 신청 조회 (본인 신청인지 확인)
        Application application = applicationRepository.findActiveById(applicationId)
                .orElseThrow(()-> new ApplicationException(HttpStatus.NOT_FOUND, "Application not found"));

        if(!application.getUser().getId().equals(user.getId())) {
            throw new ApplicationException(HttpStatus.FORBIDDEN, "Application does not belong to user");
        }

        // 신청 취소
        applicationRepository.delete(application);
    }
}
