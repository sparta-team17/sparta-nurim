package com.example.nurim.domain.review.repository;

import com.example.nurim.domain.review.dto.response.UserReviewResponse;
import com.example.nurim.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT new com.example.nurim.domain.review.dto.response.UserReviewResponse(" +
            "r.id, r.contents, r.rating, r.createdAt, p.id, p.title, p.deletedAt) " +
            "FROM Review r " +
            "JOIN r.program p " +
            "WHERE r.user.id = :userId AND r.deletedAt IS NULL")
    Page<UserReviewResponse> findActiveByUserId(@Param("userId") Long userId, Pageable pageable);
}
