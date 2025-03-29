package com.example.nurim.domain.program.service;

import com.example.nurim.domain.common.exception.CustomException;
import com.example.nurim.domain.common.exception.ErrorCode;
import com.example.nurim.domain.keyword.entity.Keyword;
import com.example.nurim.domain.keyword.repository.KeywordRepository;
import com.example.nurim.domain.program.dto.requestDto.ProgramRequestDto;
import com.example.nurim.domain.program.dto.requestDto.ProgramSearchRequestDto;
import com.example.nurim.domain.program.dto.requestDto.ProgramUpdateRequestDto;
import com.example.nurim.domain.program.dto.responseDto.*;
import com.example.nurim.domain.program.entity.Category;
import com.example.nurim.domain.program.entity.Program;
import com.example.nurim.domain.program.entity.ProgramDate;
import com.example.nurim.domain.program.enums.ProgramDateStatus;
import com.example.nurim.domain.program.enums.ProgramStatus;
import com.example.nurim.domain.program.repository.CategoryRepository;
import com.example.nurim.domain.program.repository.ProgramDateRepository;
import com.example.nurim.domain.program.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
  private final KeywordRepository keywordRepository;

  // 프로그램 등록
  @Transactional
  public ProgramResponseDto createProgram(ProgramRequestDto requestDto) {

    Category findCategory = categoryRepository.findById(requestDto.getCategoryId())
        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

    Program program = new Program(
        findCategory,
        requestDto.getTitle(),
        requestDto.getLocation(),
        requestDto.getQuota(),
        requestDto.getDetail(),
        requestDto.getStatus(),
        requestDto.getRegistrationStartDate(),
        requestDto.getRegistrationEndDate(),
        requestDto.getPhone()
    );

    programRepository.save(program);

    List<ProgramDate> programDateList = new ArrayList<>();
    // 선택된 일정들로 PromgramDate 객체 생성
    for (LocalDateTime date : requestDto.getUsageDates()) {
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
        program.getRegistrationStartDate(),
        program.getRegistrationEndDate(),
        program.getPhone(),
        program.getCreatedAt()
    );
  }

  @Transactional
  // 프로그램 목록 조회
  public Page<ProgramListRequestDto> findProgramList(ProgramSearchRequestDto requestDto) {
    Pageable pageable = PageRequest.of(requestDto.getPage() - 1, requestDto.getSize());

    if (requestDto.getTitle() != null) {
      keywordRepository.findKeywordBySearchKeyword(requestDto.getTitle())
              .ifPresentOrElse(
                  this::incrementAndSaveKeyword,
                  () -> saveNewKeyword(requestDto.getTitle())
              );
    }

    return programRepository.findProgramList(
        requestDto.getTitle(),
        requestDto.getLocation(),
        requestDto.getStatus(),
        pageable
    );
  }

  // 프로그램의 일정 조회
  public ProgramDatesResponseDto findAll(Long programId) {
    Program findProgram = programRepository.findByIdAndDeletedAtIsNull(programId)
        .orElseThrow(() -> new CustomException(ErrorCode.PROGRAM_NOT_FOUND));

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
        programDateInfoDtos
    );
  }

  // 프로그램 수정 (상태값 제외)
  @Transactional
  public ProgramUpdateResponseDto updateProgram(Long programId, ProgramUpdateRequestDto requestDto) {
    Program findProgram = programRepository.findByIdAndDeletedAtIsNull(programId)
        .orElseThrow(() -> new CustomException(ErrorCode.PROGRAM_NOT_FOUND));

    Category findCategory = categoryRepository.findById(requestDto.getCategoryId())
        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

    findProgram.update(
        findCategory,
        requestDto.getTitle(),
        requestDto.getLocation(),
        requestDto.getQuota(),
        requestDto.getDetail(),
        requestDto.getRegistrationStartDate(),
        requestDto.getRegistrationEndDate(),
        requestDto.getPhone()
    );

    return new ProgramUpdateResponseDto(
        findProgram.getId(),
        findProgram.getTitle(),
        findProgram.getLocation(),
        findProgram.getCategory().getId(),
        findProgram.getQuota(),
        findProgram.getDetail(),
        findProgram.getRegistrationStartDate(),
        findProgram.getRegistrationEndDate(),
        findProgram.getPhone(),
        findProgram.getUpdatedAt()
    );
  }

  // 프로그램 일정 수정
  @Transactional
  public ProgramDateUpdateResponseDto updateProgramDates(Long programId, List<LocalDateTime> usageDates) {

    Program findProgram = programRepository.findByIdAndDeletedAtIsNull(programId)
        .orElseThrow(() -> new CustomException(ErrorCode.PROGRAM_NOT_FOUND));
    // 기존에 있던 일정은 모두 삭제
    programDateRepository.deleteAllByProgram(findProgram);
    // 새로 프로그램 일정 추가
    List<ProgramDate> programDateList = new ArrayList<>();
    for (LocalDateTime date : usageDates) {
      programDateList.add(new ProgramDate(findProgram, date));
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
  public void deleteProgram(Long programId) {
    Program findProgram = programRepository.findByIdAndDeletedAtIsNull(programId)
        .orElseThrow(() -> new CustomException(ErrorCode.PROGRAM_NOT_FOUND));
    // 프로그램에 포함된 일정 먼저 삭제
    programDateRepository.deleteAllByProgram(findProgram);

    findProgram.delete(LocalDateTime.now());
  }

  // 프로그램 일정 삭제
  @Transactional
  public void deleteProgramDate(Long programDateId) {
    ProgramDate findProgramDate = programDateRepository.findById(programDateId)
        .orElseThrow(() -> new CustomException(ErrorCode.PROGRAMDATE_NOT_FOUND));
    programDateRepository.delete(findProgramDate);
  }

  // 프로그램 자동 상태 변경
  @Transactional
  public void updateProgramStatus(LocalDateTime now) {
    // 모집중인 일정 조회하고 정원이 다 차면 일정 마감으로 바꾸기
    List<ProgramDate> programDateList = programDateRepository.findAllByStatus(ProgramDateStatus.RECRUITING);
    for (ProgramDate date : programDateList) {
      if (date.getCount() >= date.getProgram().getQuota()) {
        date.updateClose(ProgramDateStatus.CLOSED);
      }
    }
    // 프로그램 상태 변경
    List<Program> programList = programRepository.findAllByDeletedAtIsNull();
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

  // 모든 일정 closed인지 체크 ( 하나라도 모집중 있으면 false)
  private boolean isAllDatesClosed(List<ProgramDate> dateList) {
    for (ProgramDate date : dateList) {
      if (date.getStatus() != ProgramDateStatus.CLOSED) {
        return false;
      }
    }
    return true;
  }

  // 검색 횟수를 증가시키고 저장
  private void incrementAndSaveKeyword(Keyword keyword) {
    keyword.incrementSearchCount();
    keywordRepository.save(keyword);
  }

  // 새로운 검색어 추가
  private void saveNewKeyword(String keyword) {
    Keyword newKeyword = new Keyword(keyword, 1L);
    keywordRepository.save(newKeyword);
  }
}
