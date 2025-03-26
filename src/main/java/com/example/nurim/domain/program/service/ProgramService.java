package com.example.nurim.domain.program.service;

import com.example.nurim.domain.program.dto.responseDto.*;
import com.example.nurim.domain.program.entity.Category;
import com.example.nurim.domain.program.entity.Program;

import com.example.nurim.domain.program.entity.ProgramDate;
import com.example.nurim.domain.program.enums.ProgramDateStatus;
import com.example.nurim.domain.program.enums.ProgramStatus;
import com.example.nurim.domain.program.exception.ProgramException;
import com.example.nurim.domain.program.repository.CategoryRepository;
import com.example.nurim.domain.program.repository.ProgramDateRepository;
import com.example.nurim.domain.program.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
                                          LocalDateTime usageStartDate, LocalDateTime usageEndDate, List<LocalDateTime> usageDates, LocalDateTime registrationStartDate, LocalDateTime registrationEndDate, String phone) {

    Category findCategory = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new ProgramException("존재하지 않는 카테고리 입니다", HttpStatus.NOT_FOUND));

    Program program = new Program(findCategory, title, location, quota, detail, status, usageStartDate, usageEndDate, registrationStartDate, registrationEndDate, phone);

    programRepository.save(program);

    List<ProgramDate> programDateList = new ArrayList<>();
    // 선택된 일정들로 PromgramDate 객체 생성
    for (LocalDateTime date : usageDates) {
      programDateList.add(new ProgramDate(program, date));
    }
    programDateRepository.saveAll(programDateList);


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


  // 프로그램 한번에 조회
  public ProgramDatesResponseDto findAll(Long id) {
    Program findProgram = programRepository.findByIdNotDeleted(id)
        .orElseThrow(() -> new ProgramException("존재하지 않는 프로그램입니다.", HttpStatus.NOT_FOUND));

    List<ProgramDate> programDates = programDateRepository.findAllByProgram(findProgram);

    List<ProgramDateInfoDto> programDateInfoDtos = new ArrayList<>();

    for (ProgramDate date : programDates) {
      ProgramDateInfoDto programDateInfoDto = new ProgramDateInfoDto(
          date.getDate().toLocalDate(),
          date.getCount(),
          findProgram.getQuota(),
          date.getStatus()
      );
      programDateInfoDtos.add(programDateInfoDto);
    }

    return new ProgramDatesResponseDto(
        findProgram.getId(),
        findProgram.getTitle(),
        findProgram.getLocation(),
        findProgram.getDetail(),
        findProgram.getPhone(),
        findProgram.getStatus(),
        findProgram.getQuota(),
        findProgram.getRegistrationStartDate(),
        findProgram.getRegistrationEndDate(),
        findProgram.getUsageStartDate(),
        findProgram.getUsageEndDate(),
        programDateInfoDtos
    );
  }

  // 프로그램 수정 (상태값 제외)
  @Transactional
  public ProgramUpdateResponseDto updateProgram(Long id, String title, String location, Long categoryId,
                                                Long quota, String detail, LocalDateTime usageStartDate, LocalDateTime usageEndDate,
                                                LocalDateTime registrationStartDate, LocalDateTime registrationEndDate, String phone) {
    Program findProgram = programRepository.findByIdNotDeleted(id)
        .orElseThrow(() -> new ProgramException("존재하지 않는 프로그램입니다.", HttpStatus.NOT_FOUND));

    Category findCategory = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new ProgramException("존재하지 않는 카테고리입니다.", HttpStatus.NOT_FOUND));

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

  // 프로그램 일정 수정
  @Transactional
  public ProgramDateUpdateResponseDto updateProgramDates(Long id, List<LocalDateTime> usageDates) {

    Program findProgram = programRepository.findByIdNotDeleted(id)
        .orElseThrow(() -> new ProgramException("존재하지 않는 프로그램입니다.", HttpStatus.NOT_FOUND));
    // 기존에 있던 일정은 모두 삭제
    programDateRepository.deleteAllByProgram(findProgram);

    List<ProgramDate> programDateList = new ArrayList<>();
    for (LocalDateTime date : usageDates) {
      ProgramDate programDate = new ProgramDate(findProgram, date);
      programDateList.add(programDate);
    }

    programDateRepository.saveAll(programDateList);

    List<LocalDateTime> updatedDatelist = new ArrayList<>();
    for (ProgramDate date : programDateList) {
      updatedDatelist.add(date.getDate());
    }

    return new ProgramDateUpdateResponseDto(findProgram.getId(), updatedDatelist);
  }

  // 프로그램 삭제
  @Transactional
  public void deleteProgram(Long id) {
    Program findProgram = programRepository.findByIdNotDeleted(id)
        .orElseThrow(() -> new ProgramException("존재하지 않는 프로그램입니다.", HttpStatus.NOT_FOUND));
    findProgram.delete();
  }

  // 프로그램 일정 삭제
  @Transactional
  public void deleteProgramDate(Long id) {
    ProgramDate findProgramDate = programDateRepository.findById(id)
        .orElseThrow(() -> new ProgramException("존재하지 않는 일정입니다.", HttpStatus.NOT_FOUND));
    programDateRepository.delete(findProgramDate);
  }

  // 스케줄러
  @Scheduled(cron = "10 * * * * *")
  @Transactional
  public void updateProgramDateStatus() {
    LocalDateTime now = LocalDateTime.now();
    // 모집중 조회하고 정원이 다 차면 날짜별 일정 CLOESD로 바꾸기
    List<ProgramDate> programDateList = programDateRepository.findAllByStatus(ProgramDateStatus.RECRUITING);
    for (ProgramDate date : programDateList) {
      if (date.getCount() >= date.getProgram().getQuota()) {
        date.updateClose();
      }
    }
    // 프로그램 상태 변경
    List<Program> programList = programRepository.findAllNotDeleted();
    for (Program program : programList) {
      List<ProgramDate> dateList = programDateRepository.findAllByProgram(program);
      // 현재 시각이 접수기간 보다 늦어지거나 모든 일정의 정원이 차면 프로그램 상태 COMPLTE
      if (now.isAfter(program.getRegistrationEndDate()) || isAllDatesClosed(dateList)) {
        program.updateStatus(ProgramStatus.COMPLETE);
      } else if (
          now.isAfter(program.getRegistrationStartDate()) && now.isBefore(program.getRegistrationEndDate())
      ) {
        program.updateStatus(ProgramStatus.ACCEPTING); // 접수 중
      }
    }
  }
  // 모든 일정 closed인지 체크
  private boolean isAllDatesClosed(List<ProgramDate> dateList) {
    for (ProgramDate date : dateList) {
      if (date.getStatus() != ProgramDateStatus.CLOSED) {
        return false;
      }
    }
    return true;
  }
}
