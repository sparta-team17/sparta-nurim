package com.example.nurim.domain.program.repository;

import com.example.nurim.domain.program.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long> {

    Optional<Program> findProgramByIdAndDeletedAtIsNull(@Param("programId") Long programId);

    boolean existsProgramByIdAndDeletedAtIsNull(@Param("programId") Long programId);
}
