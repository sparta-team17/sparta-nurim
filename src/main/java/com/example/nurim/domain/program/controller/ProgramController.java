package com.example.nurim.domain.program.controller;

import com.example.nurim.domain.program.dto.requestDto.ProgramDateUpdateRequestDto;
import com.example.nurim.domain.program.dto.requestDto.ProgramRequestDto;
import com.example.nurim.domain.program.dto.requestDto.ProgramUpdateRequestDto;
import com.example.nurim.domain.program.dto.responseDto.*;
import com.example.nurim.domain.program.enums.ProgramStatus;
import com.example.nurim.domain.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ProgramController {
  private final ProgramService programService;

  // 프로그램 등록
  @PostMapping("/admin/programs")
  public ResponseEntity<ProgramResponseDto> createProgram(@RequestBody ProgramRequestDto requestDto) {
    ProgramResponseDto programResponseDto = programService.createProgram(
        requestDto.getTitle(),
        requestDto.getLocation(),
        requestDto.getCategoryId(),
        requestDto.getStatus(),
        requestDto.getQuota(),
        requestDto.getDetail(),
        requestDto.getUsageDates(),
        requestDto.getRegistrationStartDate(),
        requestDto.getRegistrationEndDate(),
        requestDto.getPhone()
    );
    return new ResponseEntity<>(programResponseDto, HttpStatus.CREATED);
  }
  // 프로그램 전체 목록 조회
  @GetMapping("/programs")
  public ResponseEntity<Page<ProgramListRequestDto>> findProgramList(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String location,
      @RequestParam(required = false) ProgramStatus status,
      @RequestParam(required = false) String date,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size
  ){
    LocalDateTime dateTime = date != null ? LocalDate.parse(date).atStartOfDay() : null;

    Page<ProgramListRequestDto> programList = programService.findProgramList(title, location, status, dateTime, page, size);
    return new ResponseEntity<>(programList,HttpStatus.OK);
  }

  // 프로그램 모든 데이터 조회
  @GetMapping("/programs/{id}")
  public ResponseEntity<ProgramDatesResponseDto> findAll(@PathVariable Long id) {
    ProgramDatesResponseDto programDatesResponseDto  = programService.findAll(id);
    return new ResponseEntity<>(programDatesResponseDto,HttpStatus.OK);
  }

  // 프로그램 수정
  @PutMapping("admin/programs/{id}")
  public ResponseEntity<ProgramUpdateResponseDto> updateProgram(@PathVariable Long id, @RequestBody ProgramUpdateRequestDto requestDto) {
    ProgramUpdateResponseDto programUpdateResponseDto = programService.updateProgram(
        id,
        requestDto.getTitle(),
        requestDto.getLocation(),
        requestDto.getCategoryId(),
        requestDto.getQuota(),
        requestDto.getDetail(),
        requestDto.getRegistrationStartDate(),
        requestDto.getRegistrationEndDate(),
        requestDto.getPhone()
    );
    return new ResponseEntity<>(programUpdateResponseDto, HttpStatus.OK);
  }

  //프로그램 일정 수정
  @PutMapping("/admin/programs/{id}/dates")
  public ResponseEntity<ProgramDateUpdateResponseDto> updateProgramDates(
      @PathVariable Long id,
      @RequestBody ProgramDateUpdateRequestDto request) {

    ProgramDateUpdateResponseDto programDateUpdateResponseDto = programService.updateProgramDates(id, request.getUsageDates());
    return new ResponseEntity<>(programDateUpdateResponseDto, HttpStatus.OK);
  }

  // 프로그램 삭제
  @DeleteMapping("/admin/programs/{id}")
  public ResponseEntity<Void> deleteProgram(@PathVariable Long id) {
    programService.deleteProgram(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // 프로그램 일정 삭제
  @DeleteMapping("/admin/programs/dates/{id}")
  public ResponseEntity<Void> deleteProgramDate(@PathVariable Long id){
    programService.deleteProgramDate(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}


