package com.example.nurim.domain.program.service;

import com.example.nurim.domain.program.dto.requestDto.ProgramRequestDto;
import com.example.nurim.domain.program.dto.responseDto.ProgramResponseDto;
import com.example.nurim.domain.program.entity.Category;
import com.example.nurim.domain.program.entity.Program;
import com.example.nurim.domain.program.enums.ProgramStatus;
import com.example.nurim.domain.program.repository.CategoryRepository;
import com.example.nurim.domain.program.repository.ProgramDateRepository;
import com.example.nurim.domain.program.repository.ProgramRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


import javax.swing.text.html.Option;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProgramServiceTest {
  @Mock
  private ProgramRepository programRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private ProgramDateRepository programDateRepository;

  @InjectMocks
  private ProgramService programService;

  @Test
  void Program_생성_성공() {
    // given
    Long categoryId = 1L;
    String title = "스파르타";
    String location = "메타버스";
    ProgramStatus status = ProgramStatus.ACCEPTING;
    Long quota = 100L;
    String detail = "자바 스프링 부트 캠프";
    LocalDateTime registrationStartDate = LocalDateTime.of(2025, 3, 25, 9, 0);
    LocalDateTime registrationEndDate = LocalDateTime.of(2025, 3, 30, 18, 0);
    String phone = "010-1234-5678";
    List<LocalDateTime> usageDates = List.of(LocalDateTime.of(2025, 3, 26, 10, 0));

    ProgramRequestDto requestDto = new ProgramRequestDto(
        title, location, categoryId, status, quota, detail, usageDates, registrationStartDate, registrationEndDate, phone
    );

    Category category = new Category("코딩");
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
    Program program = new Program(category, title, location, quota, detail, status, registrationStartDate, registrationEndDate, phone);

    when(programRepository.save(any(Program.class))).thenReturn(program);

    // when
    ProgramResponseDto responseDto = programService.createProgram(requestDto);
    // then
    assertThat(responseDto).isNotNull();
    assertThat(responseDto.getTitle()).isEqualTo("스파르타");

  }

  @Test
  void CatecoryId를_조회하지_못해서_생성_실패() {
    // given
    Long categoryId = 1L;
    String title = "스파르타";
    String location = "메타버스";
    ProgramStatus status = ProgramStatus.ACCEPTING;
    Long quota = 100L;
    String detail = "자바 스프링 부트 캠프";
    LocalDateTime registrationStartDate = LocalDateTime.of(2025, 3, 25, 9, 0);
    LocalDateTime registrationEndDate = LocalDateTime.of(2025, 3, 30, 18, 0);
    String phone = "010-1234-5678";
    List<LocalDateTime> usageDates = List.of(LocalDateTime.of(2025, 3, 26, 10, 0));

    ProgramRequestDto requestDto = new ProgramRequestDto(
        title, location, categoryId, status, quota, detail, usageDates, registrationStartDate, registrationEndDate, phone
    );

    when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

    // when & then
  }
}