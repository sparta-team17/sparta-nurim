package com.example.nurim.domain.program.controller;

import com.example.nurim.domain.program.dto.requestDto.ProgramSearchRequestDto;
import com.example.nurim.domain.program.dto.responseDto.*;

import com.example.nurim.domain.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/programs")
public class ProgramController {

  private final ProgramService programService;

  // 프로그램 전체 목록 조회
  @GetMapping
  public ResponseEntity<Page<ProgramListRequestDto>> findProgramList(@ModelAttribute ProgramSearchRequestDto requestDto){
    Page<ProgramListRequestDto> programList = programService.findProgramList(requestDto);
    return new ResponseEntity<>(programList,HttpStatus.OK);
  }

  // 프로그램 모든 데이터 조회
  @GetMapping("/{programId}")
  public ResponseEntity<ProgramDatesResponseDto> findAll(@PathVariable Long programId) {
    ProgramDatesResponseDto programDatesResponseDto  = programService.findAll(programId);
    return new ResponseEntity<>(programDatesResponseDto,HttpStatus.OK);
  }

}


