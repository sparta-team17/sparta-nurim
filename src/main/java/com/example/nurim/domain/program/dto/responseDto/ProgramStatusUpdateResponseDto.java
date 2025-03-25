package com.example.nurim.domain.program.dto.responseDto;

import com.example.nurim.domain.program.enums.ProgramStatus;
import lombok.Getter;

@Getter
public class ProgramStatusUpdateResponseDto {
  ProgramStatus status;

  public ProgramStatusUpdateResponseDto(ProgramStatus status){
    this.status = status;
  }
}
