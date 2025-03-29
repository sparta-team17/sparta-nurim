package com.example.nurim.domain.application.repository;

import com.example.nurim.domain.application.dto.response.UserApplicationResponse;
import com.example.nurim.domain.application.entity.Application;
import com.example.nurim.domain.user.dto.request.FindApplicationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicationRepository extends JpaRepository<Application, Long>, ApplicationCustomRepository {

    @Query("""
            SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END
            FROM Application a
            JOIN a.programDate d
            WHERE a.user.id = :userId AND a.status = 'COMPLETE' AND d.id = :programDateId
    """)
    boolean existsByProgramDateIdAndUserId(@Param("programDateId") Long programDateId, @Param("userId") Long userId);

    @Query("""
            SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END
            FROM Application a
            JOIN a.programDate d
            WHERE a.user.id = :userId AND a.status = 'COMPLETE' AND d.date > CURRENT_TIMESTAMP
    """)
    Boolean existsUnusedApplicationByUserId(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"programDate.program"})
    Page<UserApplicationResponse> findByUserId(Long userId, FindApplicationRequest request, Pageable pageable);

    long countByProgramDateId(Long programDateId);
}
