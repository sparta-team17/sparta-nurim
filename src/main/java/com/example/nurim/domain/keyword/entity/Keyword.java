package com.example.nurim.domain.keyword.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "keywords")
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "search_keyword", nullable = false, length = 100)
    private String searchKeyword;

    @Column(nullable = false)
    private Long searchCount = 0L;

    @Column(name = "searched_at", nullable = false)
    private LocalDateTime searchedAt;

    public Keyword(String searchKeyword, Long searchCount, LocalDateTime searchedAt) {
        this.searchKeyword = searchKeyword;
        this.searchCount = searchCount;
        this.searchedAt = searchedAt;
    }

    public void incrementSearchCount() {
        this.searchCount++;
    }
}
