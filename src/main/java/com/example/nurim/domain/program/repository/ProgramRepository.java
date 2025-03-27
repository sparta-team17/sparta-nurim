package com.example.nurim.domain.program.repository;

import com.example.nurim.domain.program.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long>, ProgramRepositoryQuery {
  
  // 삭제된 프로그램 빼고 모두 조회
  List<Program> findAllByDeletedAtIsNull();
  
  Optional<Program> findByIdAndDeletedAtIsNull(Long id);
  
  Optional<Program> findProgramByIdAndDeletedAtIsNull(@Param("programId") Long programId);
  
  boolean existsProgramByIdAndDeletedAtIsNull(@Param("programId") Long programId);
}
