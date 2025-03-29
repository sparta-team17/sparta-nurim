package com.example.nurim.domain.keyword.entity;

import com.example.nurim.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "keywords")
public class Keyword extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "search_keyword", nullable = false, length = 100)
    private String searchKeyword;

    @Column(nullable = false)
    private Long searchCount = 0L;

    public Keyword(String searchKeyword, Long searchCount) {
        this.searchKeyword = searchKeyword;
        this.searchCount = searchCount;
    }

    public void incrementSearchCount() {
        this.searchCount++;
    }
}
