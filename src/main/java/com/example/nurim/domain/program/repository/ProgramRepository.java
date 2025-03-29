package com.example.nurim.domain.program.repository;

import com.example.nurim.domain.program.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

  // 조회수 증가
  @Modifying
  @Query("UPDATE Program p SET p.viewCount = p.viewCount + 1 WHERE p.id = :programId")
  void incrementViewCount(@Param("programId") Long programId);

  // 조회수 가져오기
  @Query("SELECT p.viewCount FROM Program p WHERE p.id = :programId AND p.deletedAt IS NULL")
  Long getViewCount(@Param("programId") Long programId);

  // 프로그램 아이디 모두 조회
  @Query("SELECT p.id FROM Program p WHERE p.deletedAt IS NULL")
  List<Long> findAllProgramIds();
}
