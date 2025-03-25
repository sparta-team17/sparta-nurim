package com.example.nurim.domain.auth.service;

import com.example.nurim.config.JwtUtil;
import com.example.nurim.domain.auth.dto.request.SignupRequest;
import com.example.nurim.domain.auth.dto.response.AuthResponse;
import com.example.nurim.domain.auth.exception.AuthException;
import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse signup(SignupRequest request) {
        validateEmailInUse(request.getEmail());
        validateDeletedAccount(request.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), encodedPassword, request.getName());

        userRepository.save(user);

        String token = jwtUtil.createToken(user.getId(), user.getEmail(), user.getName(), user.getRole());
        return new AuthResponse(token);
    }

    private void validateEmailInUse(String email) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(email)) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "This email is already in use");
        }
    }

    private void validateDeletedAccount(String email) {
        if (userRepository.existsByEmailAndDeletedAtIsNotNull(email)) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "This account has been deleted");
        }
    }
}
