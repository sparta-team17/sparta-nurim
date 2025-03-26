package com.example.nurim.domain.program.dto.responseDto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProgramDateUpdateResponseDto {
  private Long id;
  private List<LocalDateTime> usageDates;

  public ProgramDateUpdateResponseDto(Long id, List<LocalDateTime> usageDates) {
    this.id = id;
    this.usageDates = usageDates;
  }

}
