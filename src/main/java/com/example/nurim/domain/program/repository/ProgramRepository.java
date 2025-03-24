package com.example.nurim.domain.program.repository;

import com.example.nurim.domain.program.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, Long> {
}
