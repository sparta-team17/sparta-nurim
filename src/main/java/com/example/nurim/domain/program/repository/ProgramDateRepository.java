package com.example.nurim.domain.program.repository;

import com.example.nurim.domain.program.entity.Program;
import com.example.nurim.domain.program.entity.ProgramDate;
import com.example.nurim.domain.program.enums.ProgramDateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProgramDateRepository extends JpaRepository<ProgramDate, Long> {
  // 특정 프로그램의 모든 일정 조회
  List<ProgramDate> findAllByProgram(Program program);
  // 특정 프로그램의 모든 일정 삭제
  List<ProgramDate> deleteAllByProgram(Program program);
  // 마감되지 않은 일정 조회 위해서
  List<ProgramDate> findAllByStatus(ProgramDateStatus status);
}
