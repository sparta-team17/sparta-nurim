package com.example.nurim.domain.program.dto.requestDto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProgramUpdateRequestDto {
  private final String title;
  private final String location;
  private final Long categoryId;
  private final Long quota;
  private final String detail;
  private final LocalDateTime usageStartDate;
  private final LocalDateTime usageEndDate;
  private final LocalDateTime registrationStartDate;
  private final LocalDateTime registrationEndDate;
  private final String phone;

  public ProgramUpdateRequestDto(String title, String location, Long categoryId, Long quota,
                                 String detail, LocalDateTime usageStartDate, LocalDateTime usageEndDate,
                                 LocalDateTime registrationStartDate, LocalDateTime registrationEndDate,
                                 String phone) {
    this.title = title;
    this.location = location;
    this.categoryId = categoryId;
    this.quota = quota;
    this.detail = detail;
    this.usageStartDate = usageStartDate;
    this.usageEndDate = usageEndDate;
    this.registrationStartDate = registrationStartDate;
    this.registrationEndDate = registrationEndDate;
    this.phone = phone;
  }
}
