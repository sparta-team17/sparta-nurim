package com.example.nurim.domain.program.repository;

import com.example.nurim.domain.program.entity.Program;
import com.example.nurim.domain.program.entity.ProgramDate;
import com.example.nurim.domain.program.enums.ProgramDateStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProgramDateRepository extends JpaRepository<ProgramDate, Long> {
  // 프로그램 모든 일정 조회
  List<ProgramDate> findAllByProgram(Program program);
  List<ProgramDate> deleteAllByProgram(Program program);
  // 마감 안된 일정 조회하기 위해서
  List<ProgramDate> findAllByStatus(ProgramDateStatus status);




}
