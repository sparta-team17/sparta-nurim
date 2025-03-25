package com.example.nurim.domain.program.controller;

import com.example.nurim.domain.program.dto.requestDto.ProgramRequestDto;
import com.example.nurim.domain.program.dto.requestDto.ProgramUpdateRequestDto;
import com.example.nurim.domain.program.dto.responseDto.ProgramDetailResponseDto;
import com.example.nurim.domain.program.dto.responseDto.ProgramListResponseDto;
import com.example.nurim.domain.program.dto.responseDto.ProgramResponseDto;
import com.example.nurim.domain.program.dto.responseDto.ProgramUpdateResponseDto;
import com.example.nurim.domain.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProgramController {
  private final ProgramService programService;
  // 프로그램 등록
  @PostMapping("/admin/programs")
  public ResponseEntity<ProgramResponseDto> createProgram(@RequestBody ProgramRequestDto requestDto){
    ProgramResponseDto programResponseDto = programService.createProgram(requestDto.getTitle(), requestDto.getLocation(), requestDto.getCategoryId(), requestDto.getStatus(), requestDto.getQuota(), requestDto.getDetail(), requestDto.getUsageStartDate(), requestDto.getUsageEndDate(), requestDto.getRegistrationStartDate(), requestDto.getRegistrationEndDate(), requestDto.getPhone());
    return new ResponseEntity<>(programResponseDto, HttpStatus.CREATED);
  }

  // 프로그램 목록 조회
  @GetMapping("/programs")
  public ResponseEntity<List<ProgramListResponseDto>> findAll(){
    List<ProgramListResponseDto> programList = programService.findAll();
    return new ResponseEntity<>(programList, HttpStatus.OK);
  }

  // 프로그램 상세 조회
  @GetMapping("/programs/{id}")
  public ResponseEntity<ProgramDetailResponseDto> findByID(
      @PathVariable Long id, @RequestParam(name = "date") String date) {
      LocalDateTime dateTime = LocalDate.parse(date).atStartOfDay();

    ProgramDetailResponseDto programDetailResponseDto = programService.findById(id, dateTime);
    return new ResponseEntity<>(programDetailResponseDto,HttpStatus.OK);
  }

  // 프로그램 수정
  @PutMapping("admin/programs/{id}")
  public ResponseEntity<ProgramUpdateResponseDto> updateProgram(@PathVariable Long id, @RequestBody ProgramUpdateRequestDto requestDto){
    ProgramUpdateResponseDto programUpdateResponseDto = programService.updateProgram(
        id,
        requestDto.getTitle(),
        requestDto.getLocation(),
        requestDto.getCategoryId(),
        requestDto.getQuota(),
        requestDto.getDetail(),
        requestDto.getUsageStartDate(),
        requestDto.getUsageEndDate(),
        requestDto.getRegistrationStartDate(),
        requestDto.getRegistrationEndDate(),
        requestDto.getPhone()
    );
    return new ResponseEntity<>(programUpdateResponseDto, HttpStatus.OK);
  }


}
