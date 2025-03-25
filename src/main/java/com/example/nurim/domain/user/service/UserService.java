package com.example.nurim.domain.user.service;

import com.example.nurim.domain.user.dto.request.UpdateNameRequest;
import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void updateName(Long userId, UpdateNameRequest request) {
        User user = userRepository.findActiveByIdOrElseThrow(userId);
        user.setName(request.getName());
    }
}
