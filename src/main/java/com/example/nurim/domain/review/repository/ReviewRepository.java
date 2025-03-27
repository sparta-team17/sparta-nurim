package com.example.nurim.domain.review.repository;

import com.example.nurim.domain.review.dto.response.UserReviewResponse;
import com.example.nurim.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
            SELECT r.id AS reviewId, r.contents AS contents, r.rating AS rating, r.createdAt AS createdAt, 
                   p.id AS programId, p.title AS programTitle, p.deletedAt AS programDeletedAt
            FROM Review r 
            JOIN r.program p 
            WHERE r.user.id = :userId AND r.deletedAt IS NULL
    """)
    Page<UserReviewResponse> findActiveByUserId(@Param("userId") Long userId, Pageable pageable);
}
