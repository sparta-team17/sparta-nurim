package com.example.nurim.domain.program.dto.requestDto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProgramDateUpdateRequestDto {
  private List<LocalDateTime> usageDates;
}
