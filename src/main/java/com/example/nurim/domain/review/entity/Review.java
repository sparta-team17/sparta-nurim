package com.example.nurim.domain.review.entity;

import com.example.nurim.domain.common.entity.Timestamped;
import com.example.nurim.domain.program.entity.ProgramDate;
import com.example.nurim.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
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
    @Min(0)
    @Max(5)
    private double rating;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_date_id", nullable = false)
    private ProgramDate programDate;

    private LocalDateTime deletedAt;

    @Builder
    public Review(double rating, String contents, User user, ProgramDate programDate) {
        this.rating = rating;
        this.contents = contents;
        this.user = user;
        this.programDate = programDate;
    }

    public void updateReview(double rating, String contents) {
        this.rating = rating;
        this.contents = contents;
    }

    public void deleteReview(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
