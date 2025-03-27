package com.example.nurim.domain.program.repository;

import com.example.nurim.domain.program.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
