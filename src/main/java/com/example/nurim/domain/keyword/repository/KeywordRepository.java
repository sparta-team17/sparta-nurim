package com.example.nurim.domain.keyword.repository;

import com.example.nurim.domain.keyword.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Optional<Keyword> findKeywordBySearchKeyword(String keyword);

    @Query("SELECT k, k.searchCount FROM Keyword k WHERE k.createdAt >= :since ORDER BY k.searchCount DESC LIMIT :rankSize")
    List<Keyword> findByOrderBySearchCountDesc(@Param("since") LocalDateTime since, @Param("rankSize") int rankSize);

}
