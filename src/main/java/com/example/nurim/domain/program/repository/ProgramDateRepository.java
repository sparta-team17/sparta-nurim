package com.example.nurim.domain.program.repository;

import com.example.nurim.domain.program.entity.Program;
import com.example.nurim.domain.program.entity.ProgramDate;
import com.example.nurim.domain.program.enums.ProgramDateStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProgramDateRepository extends JpaRepository<ProgramDate, Long> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT pd FROM ProgramDate pd WHERE pd.id = :id")
  Optional<ProgramDate> findByIdWithPessimisticLock(Long id);
  // 프로그램 모든 일정 조회
  List<ProgramDate> findAllByProgram(Program program);
  List<ProgramDate> deleteAllByProgram(Program program);
  // 마감 안된 일정 조회하기 위해서
  List<ProgramDate> findAllByStatus(ProgramDateStatus status);




}
