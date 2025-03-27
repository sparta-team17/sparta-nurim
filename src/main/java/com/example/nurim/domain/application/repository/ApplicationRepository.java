package com.example.nurim.domain.application.repository;


import com.example.nurim.domain.application.entity.Application;
import com.example.nurim.domain.application.exception.ApplicationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findActiveByProgramDateIdAndUserId(Long programDateId, Long userId);

    default Application findActiveApplicationOrElseThrow(Long programDateId, Long userId) {
        return findActiveByProgramDateIdAndUserId(programDateId, userId).orElseThrow(()
                -> new ApplicationException(HttpStatus.NOT_FOUND, "Application not found"));
    }

    boolean existsByProgramDateIdAndUserId(Long programDateId, Long userId);

    Optional<Application> findActiveById(Long applicationId);
}
