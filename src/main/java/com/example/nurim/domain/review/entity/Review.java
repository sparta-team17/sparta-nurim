package com.example.nurim.domain.review.entity;

import com.example.nurim.domain.common.entity.Timestamped;
import com.example.nurim.domain.program.entity.Program;
import com.example.nurim.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "reviews")
public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double rating;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    private LocalDateTime deletedAt;

    public Review(double rating, String contents, User user, Program program, LocalDateTime deletedAt) {
        this.rating = rating;
        this.contents = contents;
        this.user = user;
        this.program = program;
        this.deletedAt = deletedAt;
    }
}
