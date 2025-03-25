package com.example.nurim.domain.program.dto.requestDto;

import com.example.nurim.domain.program.dto.responseDto.ProgramStatusUpdateResponseDto;
import com.example.nurim.domain.program.enums.ProgramStatus;
import lombok.Getter;

@Getter
public class ProgramUpdateStatusRequestDto {
  private  ProgramStatus status;

  public ProgramUpdateStatusRequestDto(ProgramStatus status){
    this.status = status;
  }
}
