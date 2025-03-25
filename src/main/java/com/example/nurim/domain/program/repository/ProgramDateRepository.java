package com.example.nurim.domain.program.repository;

import com.example.nurim.domain.program.entity.Program;
import com.example.nurim.domain.program.entity.ProgramDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProgramDateRepository extends JpaRepository<ProgramDate, Long> {
  Optional<ProgramDate> findByProgramAndDate(Program program, LocalDateTime date);
}
