package com.example.nurim.domain.keyword.repository;

import com.example.nurim.domain.keyword.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
}
