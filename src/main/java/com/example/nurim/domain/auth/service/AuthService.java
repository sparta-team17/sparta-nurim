package com.example.nurim.domain.auth.service;

import com.example.nurim.config.JwtUtil;
import com.example.nurim.domain.auth.dto.request.RefreshRequest;
import com.example.nurim.domain.auth.dto.request.SigninRequest;
import com.example.nurim.domain.auth.dto.request.SignupRequest;
import com.example.nurim.domain.auth.dto.response.AuthResponse;
import com.example.nurim.domain.auth.entity.UserInfo;
import com.example.nurim.domain.common.exception.CustomException;
import com.example.nurim.domain.common.exception.ErrorCode;
import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signup(SignupRequest request) {
        validateEmailInUse(request.getEmail());
        validateDeletedAccount(request.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), encodedPassword, request.getName());

        userRepository.save(user);
    }

    @Transactional
    public AuthResponse signin(SigninRequest request) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOT_FOUND));

        validatePassword(request.getPassword(), user.getPassword());

        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getName(), user.getRole());
        String refreshToken = refreshTokenService.createRefreshToken(UserInfo.of(user));
        return new AuthResponse(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse refresh(RefreshRequest request) {
        UserInfo userInfo = refreshTokenService.extractUserInfo(request.getRefreshToken());
        String accessToken = jwtUtil.createAccessToken(userInfo.getId(), userInfo.getEmail(), userInfo.getName(), userInfo.getRole());
        return new AuthResponse(accessToken, request.getRefreshToken());
    }

    private void validateEmailInUse(String email) {
        if (userRepository.existsByEmailAndDeletedAtIsNull(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_IN_USE);
        }
    }

    private void validateDeletedAccount(String email) {
        if (userRepository.existsByEmailAndDeletedAtIsNotNull(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_DELETED);
        }
    }

    private void validatePassword(String inputPassword, String storedPassword) {
        if (!passwordEncoder.matches(inputPassword, storedPassword)) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }
    }
}
