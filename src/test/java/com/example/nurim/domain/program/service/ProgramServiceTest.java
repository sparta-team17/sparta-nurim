package com.example.nurim.domain.program.service;

import com.example.nurim.domain.common.exception.CustomException;
import com.example.nurim.domain.program.dto.requestDto.ProgramRequestDto;
import com.example.nurim.domain.program.dto.requestDto.ProgramSearchRequestDto;
import com.example.nurim.domain.program.dto.requestDto.ProgramUpdateRequestDto;
import com.example.nurim.domain.program.dto.responseDto.*;
import com.example.nurim.domain.program.entity.Category;
import com.example.nurim.domain.program.entity.Program;
import com.example.nurim.domain.program.entity.ProgramDate;
import com.example.nurim.domain.program.enums.ProgramDateStatus;
import com.example.nurim.domain.program.enums.ProgramStatus;
import com.example.nurim.domain.program.repository.CategoryRepository;
import com.example.nurim.domain.program.repository.ProgramDateRepository;
import com.example.nurim.domain.program.repository.ProgramRepository;
import com.example.nurim.domain.program.repository.ProgramViewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


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

  @Mock
  private ProgramViewRepository programViewRepository;

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

    CustomException exception = assertThrows(CustomException.class, () -> {
      programService.createProgram(requestDto);
    });
    assertEquals("존재하지 않는 카테고리입니다.", exception.getMessage());

  }

  @Test
  void 프로그램_목록_조회_성공() {

    String title = "스파르타";
    String location = "메타버스";
    ProgramStatus status = ProgramStatus.ACCEPTING;

    ProgramSearchRequestDto requestDto = new ProgramSearchRequestDto();
    requestDto.setTitle(title);
    requestDto.setLocation(location);
    requestDto.setStatus(status);
    requestDto.setPage(1);
    requestDto.setSize(10);

    ProgramListRequestDto responseDto = new ProgramListRequestDto(
        1L, title, location, "코딩", status, 100L,
        LocalDateTime.of(2025, 3, 25, 9, 0),
        LocalDateTime.of(2025, 3, 30, 18, 0)
    );

    Page<ProgramListRequestDto> mockPage = new PageImpl<>(List.of(responseDto));

    when(programRepository.findProgramList(any(), any(), any(), any()))
        .thenReturn(mockPage);

    Page<ProgramListRequestDto> result = programService.findProgramList(requestDto);

    assertNotNull(result);
    assertEquals(title, result.getContent().get(0).getTitle());

  }

  @Test
  void 프로그램_일정_조회_성공() {
    Long userId = 123L;
    Long programId = 1L;
    Category category = new Category("코딩");

    Program program = new Program(
        category,
        "스파르타",
        "메타버스",
        30L,
        "자바 스프링 부트 캠프",
        ProgramStatus.ACCEPTING,
        LocalDateTime.of(2025, 3, 20, 9, 0),
        LocalDateTime.of(2025, 3, 25, 18, 0),
        "010-1234-5678"
    );
    List<ProgramDate> programDateList = List.of(
        new ProgramDate(program, LocalDateTime.of(2025, 3, 21, 10, 0)),
        new ProgramDate(program, LocalDateTime.of(2025, 3, 22, 10, 0))
    );

    when(programRepository.findByIdAndDeletedAtIsNull(programId)).thenReturn(Optional.of(program));
    when(programDateRepository.findAllByProgram(program)).thenReturn(programDateList);
    when(programRepository.getViewCount(programId)).thenReturn(5L);

    when(programViewRepository.existsByUserIdAndProgramId(userId, programId)).thenReturn(false);

    ProgramDatesResponseDto result = programService.findAll(userId,programId);

    assertNotNull(result);
    assertEquals(program.getTitle(), result.getTitle());
  }

  @Test
  void ProgramId를_조회하지_못해서_일정_조회_실패() {
    Long userId = 123L;
    Long programId = 1L;
    when(programRepository.findByIdAndDeletedAtIsNull(programId)).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class, () -> {
      programService.findAll(userId,programId);
    });
    assertEquals("존재하지 않는 프로그램입니다.", exception.getMessage());
  }

  @Test
  void 프로그램_수정_성공() {
    Long programId = 1L;
    Long categoryId = 1L;
    Category category = new Category("코딩");
    Category updatedCategory = new Category("개발");
    Program program = new Program(
        category,
        "스파르타",
        "메타버스",
        30L,
        "자바 스프링 부트 캠프",
        ProgramStatus.ACCEPTING,
        LocalDateTime.of(2025, 3, 20, 9, 0),
        LocalDateTime.of(2025, 3, 25, 18, 0),
        "010-1234-5678"
    );

    ProgramUpdateRequestDto requestDto = new ProgramUpdateRequestDto(
        "스파르타2",
        "메타버스2",
        categoryId,
        30L,
        "자바 스프링 부트 캠프2",
        LocalDateTime.of(2025, 3, 20, 9, 0),
        LocalDateTime.of(2025, 3, 25, 18, 0),
        "010-0234-5678"
    );

    when(programRepository.findByIdAndDeletedAtIsNull(programId)).thenReturn(Optional.of(program));
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(updatedCategory));

    ProgramUpdateResponseDto result = programService.updateProgram(programId, requestDto);

    assertNotNull(result);
    assertEquals("스파르타2", result.getTitle());
  }

  @Test
  void ProgramId를_조회하지_못해서_수정_실패() {
    Long prgramId = 1L;
    Long categoryId = 1L;
    ProgramUpdateRequestDto requestDto = new ProgramUpdateRequestDto(
        "스파르타2",
        "메타버스2",
        categoryId,
        30L,
        "자바 스프링 부트 캠프2",
        LocalDateTime.of(2025, 3, 20, 9, 0),
        LocalDateTime.of(2025, 3, 25, 18, 0),
        "010-0234-5678"
    );
    when(programRepository.findByIdAndDeletedAtIsNull(prgramId)).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class, () -> {
      programService.updateProgram(prgramId, requestDto);
    });

    assertEquals("존재하지 않는 프로그램입니다.", exception.getMessage());
  }

  @Test
  void CategoryId를_조회하지_못해서_수정_실패() {
    Long programId = 1L;
    Long categoryId = 1L;

    Category category = new Category("코딩");
    Program program = new Program(
        category,
        "스파르타",
        "메타버스",
        30L,
        "자바 스프링 부트 캠프",
        ProgramStatus.ACCEPTING,
        LocalDateTime.of(2025, 3, 20, 9, 0),
        LocalDateTime.of(2025, 3, 25, 18, 0),
        "010-1234-5678"
    );
    ProgramUpdateRequestDto requestDto = new ProgramUpdateRequestDto(
        "스파르타2",
        "메타버스2",
        categoryId,
        30L,
        "자바 스프링 부트 캠프2",
        LocalDateTime.of(2025, 3, 20, 9, 0),
        LocalDateTime.of(2025, 3, 25, 18, 0),
        "010-0234-5678"
    );

    when(programRepository.findByIdAndDeletedAtIsNull(programId)).thenReturn(Optional.of(program));
    when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class, () -> {
      programService.updateProgram(programId, requestDto);
    });
    assertEquals("존재하지 않는 카테고리입니다.", exception.getMessage());
  }

  @Test
  void 프로그램_일정_수정_성공() {

    Long programId = 1L;
    Category category = new Category("코딩");

    Program program = new Program(
        category,
        "스파르타",
        "메타버스",
        30L,
        "자바 스프링 부트 캠프",
        ProgramStatus.ACCEPTING,
        LocalDateTime.of(2025, 3, 20, 9, 0),
        LocalDateTime.of(2025, 3, 25, 18, 0),
        "010-1234-5678"
    );
    List<LocalDateTime> dates = List.of(
        LocalDateTime.of(2025, 3, 20, 10, 0),
        LocalDateTime.of(2025, 3, 21, 10, 0)
    );
    when(programRepository.findByIdAndDeletedAtIsNull(programId)).thenReturn(Optional.of(program));

    ProgramDateUpdateResponseDto result = programService.updateProgramDates(programId, dates);

    assertNotNull(result);
    assertEquals(LocalDateTime.of(2025, 3, 20, 10, 0), result.getUsageDates().get(0));

  }

  @Test
  void ProgramId를_조회하지_못해서_일정_수정_실패() {

    Long programId = 1L;
    List<LocalDateTime> dates = List.of(
        LocalDateTime.of(2025, 3, 20, 10, 0),
        LocalDateTime.of(2025, 3, 21, 10, 0)
    );

    when(programRepository.findByIdAndDeletedAtIsNull(programId)).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class, () -> {
      programService.updateProgramDates(programId, dates);
    });
    assertEquals("존재하지 않는 프로그램입니다.", exception.getMessage());

  }

  @Test
  void 프로그램_삭제_성공() {
    Long programId = 1L;
    Category category = new Category("코딩");

    Program program = new Program(
        category,
        "스파르타",
        "메타버스",
        30L,
        "자바 스프링 부트 캠프",
        ProgramStatus.ACCEPTING,
        LocalDateTime.of(2025, 3, 20, 9, 0),
        LocalDateTime.of(2025, 3, 25, 18, 0),
        "010-1234-5678"
    );

    when(programRepository.findByIdAndDeletedAtIsNull(programId)).thenReturn(Optional.of(program));

    programService.deleteProgram(programId);

    assertNotNull(program.getDeletedAt());

  }

  @Test
  void ProgramId를_조회하지_못해서_삭제_실패() {
    Long programId = 1L;

    when(programRepository.findByIdAndDeletedAtIsNull(programId)).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class, () -> {
      programService.deleteProgram(programId);
    });

    assertEquals("존재하지 않는 프로그램입니다.", exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
  }

  @Test
  void 프로그램일정_삭제_성공() {
    // given
    Long programDateId = 1L;
    Category category = new Category("코딩");

    Program program = new Program(
        category,
        "스파르타",
        "메타버스",
        30L,
        "자바 스프링 부트 캠프",
        ProgramStatus.ACCEPTING,
        LocalDateTime.of(2025, 3, 20, 9, 0),
        LocalDateTime.of(2025, 3, 25, 18, 0),
        "010-1234-5678"
    );
    ProgramDate programDate = new ProgramDate(
        program,
        LocalDateTime.of(2025, 3, 21, 10, 0)
    );

    when(programDateRepository.findById(programDateId)).thenReturn(Optional.of(programDate));

    programService.deleteProgramDate(programDateId);

    verify(programDateRepository).delete(programDate);

  }

  @Test
  void ProgramDateId를_조회하지_못해서_일정_삭제_실패() {
    Long programDateId = 1L;

    when(programDateRepository.findById(programDateId)).thenReturn(Optional.empty());

    CustomException exception = assertThrows(CustomException.class, () -> {
      programService.deleteProgramDate(programDateId);
    });
    assertEquals("존재하지 않는 일정입니다.", exception.getMessage());
  }

  @Test
  void 모집중이고_정원_초관된_일정은_CLOSED로_변경() {
    Long programId = 1L;
    Category category = new Category("코딩");

    Program program = new Program(
        category,
        "스파르타",
        "메타버스",
        10L,
        "자바 스프링 부트 캠프",
        ProgramStatus.ACCEPTING,
        LocalDateTime.of(2025, 3, 20, 9, 0),
        LocalDateTime.of(2025, 3, 25, 18, 0),
        "010-1234-5678"
    );
    // 모집중이고 신청수를 정원이랑 같게 세팅
    ProgramDate programDate = new ProgramDate(program, LocalDateTime.of(2025, 3, 5, 10, 0));
    ReflectionTestUtils.setField(programDate, "status", ProgramDateStatus.RECRUITING);
    ReflectionTestUtils.setField(programDate, "count", 10);
    // 모집중 호출하면 programDate호출
    when(programDateRepository.findAllByStatus(ProgramDateStatus.RECRUITING))
        .thenReturn(List.of(programDate));

    when(programRepository.findAllByDeletedAtIsNull()).thenReturn(List.of());

    programService.updateProgramStatus(LocalDateTime.of(2025, 3, 21, 0, 0));

    assertEquals(ProgramDateStatus.CLOSED, programDate.getStatus());

  }

  @Test
  void 접수_기간이라도_모든_일정이_CLOSED되면_프로그램_상태도_COMPLETE로_변경() {
    Category category = new Category("코딩");

    Program program = new Program(
        category,
        "스파르타",
        "메타버스",
        10L,
        "자바 스프링 부트 캠프",
        ProgramStatus.ACCEPTING,
        LocalDateTime.of(2025, 3, 20, 9, 0),
        LocalDateTime.of(2025, 3, 25, 18, 0),
        "010-1234-5678"
    );
    // 일정2개 생성(마감으로)
    ProgramDate programDate1 = new ProgramDate(program, LocalDateTime.of(2025, 3, 5, 10, 0));
    programDate1.updateClose(ProgramDateStatus.CLOSED);

    ProgramDate programDate2 = new ProgramDate(program, LocalDateTime.of(2025, 3, 6, 10, 0));
    programDate2.updateClose(ProgramDateStatus.CLOSED);

    when(programRepository.findAllByDeletedAtIsNull()).thenReturn(List.of(program));
    when(programDateRepository.findAllByProgram(program)).thenReturn(List.of(programDate1, programDate2));

    programService.updateProgramStatus(LocalDateTime.of(2025, 3, 21, 0, 0));

    assertEquals(ProgramStatus.COMPLETE, program.getStatus());

  }

  @Test
  void 접수기간이_지나면_프로그램_상태는_COMPLETE로_변경() {

    Category category = new Category("코딩");

    Program program = new Program(
        category,
        "스파르타",
        "메타버스",
        10L,
        "자바 스프링 부트 캠프",
        ProgramStatus.ACCEPTING,
        LocalDateTime.of(2025, 3, 1, 9, 0),
        LocalDateTime.of(2025, 3, 5, 18, 0),
        "010-1234-5678"
    );
    ProgramDate date = new ProgramDate(program, LocalDateTime.of(2025, 3, 6, 10, 0));

    when(programDateRepository.findAllByStatus(ProgramDateStatus.RECRUITING)).thenReturn(List.of(date));
    when(programRepository.findAllByDeletedAtIsNull()).thenReturn(List.of(program));
    when(programDateRepository.findAllByProgram(program)).thenReturn(List.of(date));
    // 접수 종료일 지난 시간
    LocalDateTime now = LocalDateTime.of(2025, 3, 6, 0, 0);

    programService.updateProgramStatus(now);

    assertEquals(ProgramStatus.COMPLETE, program.getStatus());

  }

}