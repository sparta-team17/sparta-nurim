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

    @Column(name = "searched_at", nullable = false, updatable = false)
    private LocalDateTime searchedAt;

    public Keyword(String searchKeyword, LocalDateTime searchedAt) {
        this.searchKeyword = searchKeyword;
        this.searchedAt = searchedAt;
    }
}
