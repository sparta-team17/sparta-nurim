package com.example.nurim.domain.program.dto.responseDto;

import com.example.nurim.domain.program.enums.ProgramStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProgramDatesResponseDto {
  private Long id;
  private String title;
  private String location;
  private String detail;
  private String phone;
  private ProgramStatus status;
  private Long quota;
  private LocalDateTime registrationStartDate;
  private LocalDateTime registrationEndDate;
  private LocalDateTime usageStartDate;
  private LocalDateTime usageEndDate;
  private List<ProgramDateInfoDto> dates;
}
