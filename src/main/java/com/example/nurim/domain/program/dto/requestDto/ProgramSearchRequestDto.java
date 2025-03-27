package com.example.nurim.domain.program.dto.requestDto;

import com.example.nurim.domain.program.enums.ProgramStatus;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProgramSearchRequestDto {
  private String title;
  private String location;
  private ProgramStatus status;

  private int page = 1;
  private int size = 10;
}
