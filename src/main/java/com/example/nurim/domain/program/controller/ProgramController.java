package com.example.nurim.domain.program.controller;

import com.example.nurim.domain.common.dto.AuthUser;
import com.example.nurim.domain.program.dto.requestDto.ProgramSearchRequestDto;
import com.example.nurim.domain.program.dto.responseDto.ProgramDatesResponseDto;
import com.example.nurim.domain.program.dto.responseDto.ProgramListRequestDto;
import com.example.nurim.domain.program.dto.responseDto.ProgramRedisResponseDto;
import com.example.nurim.domain.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProgramController {

  private final ProgramService programService;

  // 프로그램 전체 목록 조회 V1
  @GetMapping({"/v1/programs/search"})
  public ResponseEntity<Page<ProgramListRequestDto>> findProgramListV1(@ModelAttribute ProgramSearchRequestDto requestDto){
    Page<ProgramListRequestDto> programList = programService.findProgramListV1(requestDto);
    return new ResponseEntity<>(programList,HttpStatus.OK);
  }

  // 프로그램 전체 목록 조회 V2
  @GetMapping({"/v2/programs/search"})
  public ResponseEntity<Page<ProgramListRequestDto>> findProgramListV2(@ModelAttribute ProgramSearchRequestDto requestDto){
    Page<ProgramListRequestDto> programList = programService.findProgramListV2(requestDto);
    return new ResponseEntity<>(programList,HttpStatus.OK);
  }

  // 프로그램 하나의 모든 일정까지 조회
  @GetMapping("/programs/{programId}")
  public ResponseEntity<ProgramDatesResponseDto> findAll(
      @PathVariable Long programId,
      @AuthenticationPrincipal AuthUser authUser) {

    ProgramDatesResponseDto programDatesResponseDto = programService.findAll(authUser.getId(), programId);
    return new ResponseEntity<>(programDatesResponseDto, HttpStatus.OK);
  }

  // 프로그램 하나의 모든 일정까지 조회(레디스)
  @GetMapping("/programs/{programId}/redis")
  public ResponseEntity<ProgramRedisResponseDto> findAllRedis(
      @PathVariable Long programId,
      @AuthenticationPrincipal AuthUser authUser) {
    ProgramRedisResponseDto programRedisResponseDto = programService.findAllRedis(authUser.getId(), programId);
    return new ResponseEntity<>(programRedisResponseDto, HttpStatus.OK);

  }

  // 프로그램 랭킹 조회(탑5까지만)
  @GetMapping("/programs/ranking")
  public ResponseEntity<List<String>> findProgramRangking() {
    return ResponseEntity.ok(programService.findProgramRangking());
  }

}


