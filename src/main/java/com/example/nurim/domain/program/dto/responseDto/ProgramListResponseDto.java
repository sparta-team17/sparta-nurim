package com.example.nurim.domain.program.dto.responseDto;

import com.example.nurim.domain.program.enums.ProgramStatus;
import lombok.Getter;

@Getter
public class ProgramListResponseDto {

  private final Long id;
  private final String title;
  private final String location;
  private final ProgramStatus status;
  private final Long quota;
  private final String detail;
  private final String phone;

  public ProgramListResponseDto(Long id, String title, String location, ProgramStatus status, Long quota, String detail, String phone){
    this.id = id;
    this.title = title;
    this.location = location;
    this.status = status;
    this.quota = quota;
    this.detail = detail;
    this.phone = phone;

  }
}
