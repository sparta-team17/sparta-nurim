package com.example.nurim.domain.application.repository;


import com.example.nurim.domain.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Application a " +
            "JOIN a.programDate d " +
            "WHERE a.user.id = :userId AND a.status = 'COMPLETE' AND d.date > CURRENT_TIMESTAMP")
    Boolean existsUnusedApplicationByUserId(@Param("userId") Long userId);
}
