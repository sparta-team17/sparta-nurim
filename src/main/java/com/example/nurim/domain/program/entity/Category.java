package com.example.nurim.domain.program.entity;

import com.example.nurim.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "categories")
public class Category extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime deletedAt;

    //테스트 코드용
    public Category(String name) {
        this.name = name;
    }
}
