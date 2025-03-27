package com.example.nurim.domain.program.dto.requestDto;

import com.example.nurim.domain.program.enums.ProgramStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProgramRequestDto {
  private final String title;
  private final String location;
  private final Long categoryId;
  private final ProgramStatus status;
  private final Long quota;
  private final String detail;
  private final List<LocalDateTime> usageDates;
  private final LocalDateTime registrationStartDate;
  private final LocalDateTime registrationEndDate;
  private final String phone;

  public ProgramRequestDto(String title, String location, Long categoryId, ProgramStatus status, Long quota, String detail,  List<LocalDateTime> usageDates, LocalDateTime registrationStartDate,LocalDateTime registrationEndDate, String phone){

    this.title = title;
    this.location = location;
    this.categoryId = categoryId;
    this.status = status;
    this.quota = quota;
    this.detail = detail;
    this.usageDates = usageDates;
    this.registrationStartDate = registrationStartDate;
    this.registrationEndDate = registrationEndDate;
    this.phone = phone;
  }

}
