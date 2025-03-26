package com.example.nurim.domain.program.dto.responseDto;

import com.example.nurim.domain.program.enums.ProgramStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProgramListRequestDto {
  private Long id;
  private String title;
  private String location;
  private String category;
  private ProgramStatus status;
  private Long quota;
  private LocalDateTime registrationStartDate;
  private LocalDateTime registrationEndDate;

  public ProgramListRequestDto(Long id, String title, String location, String category, ProgramStatus status,
                               Long quota, LocalDateTime registrationStartDate, LocalDateTime registrationEndDate) {
    this.id = id;
    this.title = title;
    this.location = location;
    this.category = category;
    this.status = status;
    this.quota = quota;
    this.registrationStartDate = registrationStartDate;
    this.registrationEndDate = registrationEndDate;
  }
}
