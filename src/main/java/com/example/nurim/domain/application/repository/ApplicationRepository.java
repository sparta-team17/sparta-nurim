package com.example.nurim.domain.application.repository;


import com.example.nurim.domain.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
