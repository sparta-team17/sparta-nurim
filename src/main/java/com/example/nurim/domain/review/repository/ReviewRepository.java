package com.example.nurim.domain.review.repository;

import com.example.nurim.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.program.id = :programId AND r.deletedAt IS NULL")
    Page<Review> findReviewsByProgramId(Long programId, Pageable pageable);
}
