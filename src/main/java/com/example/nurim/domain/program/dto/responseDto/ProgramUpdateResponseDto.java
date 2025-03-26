package com.example.nurim.domain.program.dto.responseDto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProgramUpdateResponseDto {
  private final Long id;
  private final String title;
  private final String location;
  private final Long categoryId;
  private final Long quota;
  private final String detail;
  private final LocalDateTime registrationStartDate;
  private final LocalDateTime registrationEndDate;
  private final String phone;
  private final LocalDateTime updatedAt;

  public ProgramUpdateResponseDto(Long id, String title, String location, Long categoryId, Long quota, String detail, LocalDateTime registrationStartDate, LocalDateTime registrationEndDate, String phone, LocalDateTime updatedAt) {
    this.id = id;
    this.title = title;
    this.location = location;
    this.categoryId = categoryId;
    this.quota = quota;
    this.detail = detail;
    this.registrationStartDate = registrationStartDate;
    this.registrationEndDate = registrationEndDate;
    this.phone = phone;
    this.updatedAt = updatedAt;
  }
}
