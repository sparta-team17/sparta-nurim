package com.example.nurim.domain.user.service;

import com.example.nurim.domain.application.repository.ApplicationRepository;
import com.example.nurim.domain.user.dto.request.UpdateNameRequest;
import com.example.nurim.domain.user.dto.request.UpdatePasswordRequest;
import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.exception.UserException;
import com.example.nurim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void updateName(Long userId, UpdateNameRequest request) {
        User user = userRepository.findActiveByIdOrElseThrow(userId);
        user.setName(request.getName());
    }

    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequest request) {
        User user = userRepository.findActiveByIdOrElseThrow(userId);

        validateCurrentPassword(request.getOldPassword(), user.getPassword());
        validateNewPassword(request.getNewPassword(), request.getOldPassword());

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findActiveByIdOrElseThrow(userId);
        validateNoUnusedApplications(userId);
        user.setDeleted();
    }

    private void validateCurrentPassword(String inputPassword, String currentPassword) {
        if (!passwordEncoder.matches(inputPassword, currentPassword)) {
            throw new UserException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }
    }

    private void validateNewPassword(String newPassword, String currentPassword) {
        if (newPassword.equals(currentPassword)) {
            throw new UserException(HttpStatus.BAD_REQUEST, "New password cannot be the same as the current password");
        }
    }

    private void validateNoUnusedApplications(Long userId) {
        if (applicationRepository.existsUnusedApplicationByUserId(userId)) {
            throw new UserException(HttpStatus.BAD_REQUEST, "Unused application exists, account deletion is not allowed");
        }
    }
}
