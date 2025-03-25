package com.example.nurim.domain.program.service;

import com.example.nurim.domain.program.dto.requestDto.ProgramUpdateRequestDto;
import com.example.nurim.domain.program.dto.responseDto.ProgramDetailResponseDto;
import com.example.nurim.domain.program.dto.responseDto.ProgramListResponseDto;
import com.example.nurim.domain.program.dto.responseDto.ProgramResponseDto;
import com.example.nurim.domain.program.dto.responseDto.ProgramUpdateResponseDto;
import com.example.nurim.domain.program.entity.Category;
import com.example.nurim.domain.program.entity.Program;

import com.example.nurim.domain.program.entity.ProgramDate;
import com.example.nurim.domain.program.enums.ProgramStatus;
import com.example.nurim.domain.program.repository.CategoryRepository;
import com.example.nurim.domain.program.repository.ProgramDateRepository;
import com.example.nurim.domain.program.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramService {
  private final ProgramRepository programRepository;
  private final CategoryRepository categoryRepository;
  private final ProgramDateRepository programDateRepository;

  // 프로그램 등록
  @Transactional
  public ProgramResponseDto createProgram(String title, String location, Long categoryId, ProgramStatus status, Long quota, String detail,
                                          LocalDateTime usageStartDate, LocalDateTime usageEndDate,
                                          LocalDateTime registrationStartDate, LocalDateTime registrationEndDate,
                                          String phone) {

    Category findCategory = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 입니다"));

    Program program = new Program(findCategory, title, location, quota, detail, status, usageStartDate, usageEndDate, registrationStartDate, registrationEndDate, phone);

    programRepository.save(program);

    return new ProgramResponseDto(
        program.getId(),
        program.getTitle(),
        program.getLocation(),
        program.getCategory().getId(),
        program.getStatus(),
        program.getQuota(),
        program.getDetail(),
        program.getUsageStartDate(),
        program.getUsageEndDate(),
        program.getRegistrationStartDate(),
        program.getRegistrationEndDate(),
        program.getPhone(),
        program.getCreatedAt()
    );
  }

  // 프로그램 목록 조회
  public List<ProgramListResponseDto> findAll() {
    List<Program> programList = programRepository.findAll();
    List<ProgramListResponseDto> responseList = new ArrayList<>();

    for (Program program : programList) {
      responseList.add(new ProgramListResponseDto(
          program.getId(),
          program.getTitle(),
          program.getLocation(),
          program.getStatus(),
          program.getQuota(),
          program.getDetail(),
          program.getPhone()
      ));
    }

    return responseList;
  }

  // 프로그램 상세 조회
  public ProgramDetailResponseDto findById(Long id, LocalDateTime date){
    Program findProgram = programRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로그램입니다."));
    // 그 날짜에 신청자 수 확인
    ProgramDate programDate = programDateRepository.findByProgramAndDate(findProgram, date).orElse(null);
    int appliedCount = (programDate != null) ? programDate.getCount() : 0;

    return new ProgramDetailResponseDto(
        findProgram.getId(),
        findProgram.getCategory().getId(),
        findProgram.getTitle(),
        findProgram.getLocation(),
        findProgram.getQuota(),
        findProgram.getDetail(),
        findProgram.getStatus(),
        findProgram.getUsageStartDate(),
        findProgram.getUsageEndDate(),
        findProgram.getRegistrationStartDate(),
        findProgram.getRegistrationEndDate(),
        appliedCount,
        findProgram.getCreatedAt(),
        findProgram.getUpdatedAt()
    );
  }
  // 프로그램 수정 (상태값 제외)
  @Transactional
  public ProgramUpdateResponseDto updateProgram(Long id, String title, String location, Long categoryId,
                                                Long quota, String detail, LocalDateTime usageStartDate, LocalDateTime usageEndDate,
                                                LocalDateTime registrationStartDate, LocalDateTime registrationEndDate, String phone) {
    Program findProgram = programRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로그램입니다."));

    Category findCategory = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

    findProgram.update(findCategory, title, location, quota, detail, usageStartDate, usageEndDate, registrationStartDate, registrationEndDate, phone);

    return new ProgramUpdateResponseDto(
        findProgram.getId(),
        findProgram.getTitle(),
        findProgram.getLocation(),
        findProgram.getCategory().getId(),
        findProgram.getQuota(),
        findProgram.getDetail(),
        findProgram.getUsageStartDate(),
        findProgram.getUsageEndDate(),
        findProgram.getRegistrationStartDate(),
        findProgram.getRegistrationEndDate(),
        findProgram.getPhone(),
        findProgram.getUpdatedAt()
    );
  }
}

