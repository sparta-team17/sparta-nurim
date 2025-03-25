package com.example.nurim.domain.program.dto.responseDto;

import com.example.nurim.domain.program.enums.ProgramStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProgramResponseDto {
  private final Long id;
  private final String title;
  private final String location;
  private final Long categoryId;
  private final ProgramStatus status;
  private final Long quota;
  private final String detail;
  private final LocalDateTime usageStartDate;
  private final LocalDateTime usageEndDate;
  private final LocalDateTime registrationStartDate;
  private final LocalDateTime registrationEndDate;
  private final String phone;
  private LocalDateTime createdAt;

  public ProgramResponseDto(Long id, String title, String location, Long categoryId, ProgramStatus status, Long quota, String detail, LocalDateTime usageStartDate, LocalDateTime usageEndDate, LocalDateTime registrationStartDate,LocalDateTime registrationEndDate, String phone, LocalDateTime createdAt ){
    this.id = id;
    this.title = title;
    this.location = location;
    this.categoryId = categoryId;
    this.status = status;
    this.quota = quota;
    this.detail = detail;
    this.usageStartDate = usageStartDate;
    this.usageEndDate =  usageEndDate;
    this.registrationStartDate = registrationStartDate;
    this.registrationEndDate = registrationEndDate;
    this.phone = phone;
    this.createdAt = createdAt;
  }

}
