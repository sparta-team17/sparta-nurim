package com.example.nurim.domain.program.entity;

import com.example.nurim.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "categories")
public class Category extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime deletedAt;
}
