package com.example.nurim.domain.program.controller;

import com.example.nurim.domain.program.dto.requestDto.ProgramDateUpdateRequestDto;
import com.example.nurim.domain.program.dto.requestDto.ProgramRequestDto;
import com.example.nurim.domain.program.dto.requestDto.ProgramUpdateRequestDto;
import com.example.nurim.domain.program.dto.responseDto.ProgramDateUpdateResponseDto;
import com.example.nurim.domain.program.dto.responseDto.ProgramResponseDto;
import com.example.nurim.domain.program.dto.responseDto.ProgramUpdateResponseDto;
import com.example.nurim.domain.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminProgramController {

  private final ProgramService programService;

  // 프로그램 등록
  @PostMapping("/programs")
  public ResponseEntity<ProgramResponseDto> createProgram(@RequestBody ProgramRequestDto requestDto) {
    ProgramResponseDto programResponseDto = programService.createProgram(requestDto);
    return new ResponseEntity<>(programResponseDto, HttpStatus.CREATED);
  }

  // 프로그램 수정
  @PutMapping("/programs/{programId}")
  public ResponseEntity<ProgramUpdateResponseDto> updateProgram(
      @PathVariable Long programId,
      @RequestBody ProgramUpdateRequestDto requestDto) {
    ProgramUpdateResponseDto programUpdateResponseDto = programService.updateProgram(programId, requestDto);
    return new ResponseEntity<>(programUpdateResponseDto, HttpStatus.OK);
  }

  //프로그램 일정 수정
  @PutMapping("/programs/{programId}/dates")
  public ResponseEntity<ProgramDateUpdateResponseDto> updateProgramDates(
      @PathVariable Long programId,
      @RequestBody ProgramDateUpdateRequestDto request) {
    ProgramDateUpdateResponseDto programDateUpdateResponseDto = programService.updateProgramDates(programId, request.getUsageDates());
    return new ResponseEntity<>(programDateUpdateResponseDto, HttpStatus.OK);
  }

  // 프로그램 삭제
  @DeleteMapping("/programs/{programId}")
  public ResponseEntity<Void> deleteProgram(@PathVariable Long programId) {
    programService.deleteProgram(programId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // 프로그램 일정 삭제
  @DeleteMapping("/programs/dates/{programDateId}")
  public ResponseEntity<Void> deleteProgramDate(@PathVariable Long programDateId) {
    programService.deleteProgramDate(programDateId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
