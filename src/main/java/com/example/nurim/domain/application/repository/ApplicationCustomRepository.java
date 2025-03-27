package com.example.nurim.domain.application.repository;

import com.example.nurim.domain.application.dto.response.UserApplicationResponse;
import com.example.nurim.domain.user.dto.request.FindApplicationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationCustomRepository {
    Page<UserApplicationResponse> findByUserId(Long userId, FindApplicationRequest request, Pageable pageable);
}
