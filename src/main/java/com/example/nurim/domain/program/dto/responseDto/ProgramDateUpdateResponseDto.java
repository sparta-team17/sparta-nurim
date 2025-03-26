package com.example.nurim.domain.program.dto.responseDto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProgramDateUpdateResponseDto {
  private final Long id;
  private final List<LocalDateTime> usageDates;

  public ProgramDateUpdateResponseDto(Long id, List<LocalDateTime> usageDates) {
    this.id = id;
    this.usageDates = usageDates;
  }

}
