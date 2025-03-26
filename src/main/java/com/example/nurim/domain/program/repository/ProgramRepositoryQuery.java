package com.example.nurim.domain.program.repository;

import com.example.nurim.domain.program.dto.responseDto.ProgramListRequestDto;
import com.example.nurim.domain.program.enums.ProgramStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ProgramRepositoryQuery {
  Page<ProgramListRequestDto> findProgramList(
      String title, String location,
      ProgramStatus status,
      LocalDateTime dateTime,
      Pageable pageable
  );
}
