package com.example.nurim.domain.program.dto.responseDto;

import com.example.nurim.domain.program.enums.ProgramDateStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ProgramDateInfoDto {
  private LocalDate date;
  private int appliedCount; // 현재 지원 인원
  private Long quota; //정원
  private ProgramDateStatus status;
}
