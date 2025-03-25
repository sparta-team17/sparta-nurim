package com.example.nurim.domain.program.dto.responseDto;

import com.example.nurim.domain.program.enums.ProgramStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProgramDetailResponseDto {
  private Long id;
  private Long categoryId;
  private String title;
  private String location;
  private Long quota;
  private String detail;
  private ProgramStatus status;
  private LocalDateTime usageStartDate;
  private LocalDateTime usageEndDate;
  private LocalDateTime registrationStartDate;
  private LocalDateTime registrationEndDate;
  private Integer appliedCount;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public ProgramDetailResponseDto(Long id, Long categoryId, String title, String location, Long quota,
                                  String detail, ProgramStatus status, LocalDateTime usageStartDate,
                                  LocalDateTime usageEndDate, LocalDateTime registrationStartDate,
                                  LocalDateTime registrationEndDate, Integer appliedCount,
                                  LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.categoryId = categoryId;
    this.title = title;
    this.location = location;
    this.quota = quota;
    this.detail = detail;
    this.status = status;
    this.usageStartDate = usageStartDate;
    this.usageEndDate = usageEndDate;
    this.registrationStartDate = registrationStartDate;
    this.registrationEndDate = registrationEndDate;
    this.appliedCount = appliedCount;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

}
