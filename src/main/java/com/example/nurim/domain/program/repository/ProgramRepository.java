package com.example.nurim.domain.program.repository;

import com.example.nurim.domain.program.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long> {
  // 삭제된 프로그램 빼고 모두 조회
  @Query("SELECT p FROM Program p WHERE p.deletedAt Is NULL")
  List<Program> findAllNotDeleted();

  @Query("SELECT p FROM Program p WHERE p.id = :id AND p.deletedAt IS NULL")
  Optional<Program> findByIdNotDeleted(@Param("id") Long id);

}
