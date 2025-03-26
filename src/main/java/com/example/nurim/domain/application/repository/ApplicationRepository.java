package com.example.nurim.domain.application.repository;


import com.example.nurim.domain.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("SELECT a FROM Application a " +
            "JOIN FETCH a.user " +
            "JOIN FETCH a.programDate pd " +
            "JOIN FETCH pd.program p " +
            "WHERE a.user.id = :userId AND p.id = :programId")
    Optional<Application> findApplicationByUserIdAndProgramId(Long userId, Long programId);

    @Query("SELECT COUNT(a) > 0 FROM Application a WHERE a.user.id = :userId AND a.programDate.program.id = :programId")
    boolean existsByUserIdAndProgramId(Long userId, Long programId);
}
