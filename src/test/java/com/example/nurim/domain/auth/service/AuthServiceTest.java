package com.example.nurim.domain.auth.service;

import com.example.nurim.config.JwtUtil;
import com.example.nurim.domain.auth.dto.request.SignupRequest;
import com.example.nurim.domain.auth.dto.response.AuthResponse;
import com.example.nurim.domain.auth.exception.AuthException;
import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.enums.UserRole;
import com.example.nurim.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthService authService;

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SignUpTests {

        private final SignupRequest request = new SignupRequest("temp@gmail.com", "password", "name");

        @Test
        @Order(1)
        void 회원가입_사용_중인_이메일이면_실패() {
            given(userRepository.existsByEmailAndDeletedAtIsNull(anyString())).willReturn(true);

            AuthException thrown = assertThrows(AuthException.class, () -> authService.signup(request));
            assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
            assertEquals("This email is already in use", thrown.getMessage());
        }

        @Test
        @Order(2)
        void 회원가입_탈퇴한_사용자의_이메일이면_실패() {
            given(userRepository.existsByEmailAndDeletedAtIsNull(anyString())).willReturn(false);
            given(userRepository.existsByEmailAndDeletedAtIsNotNull(anyString())).willReturn(true);

            AuthException thrown = assertThrows(AuthException.class, () -> authService.signup(request));
            assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
            assertEquals("This account has been deleted", thrown.getMessage());
        }

        @Test
        @Order(3)
        void 회원가입_성공() {
            String encodedPassword = "encodedPassword";
            String token = "testToken";

            given(userRepository.existsByEmailAndDeletedAtIsNull(anyString())).willReturn(false);
            given(userRepository.existsByEmailAndDeletedAtIsNotNull(anyString())).willReturn(false);
            given(passwordEncoder.encode(anyString())).willReturn(encodedPassword);
            given(jwtUtil.createToken(any(), anyString(), anyString(), any(UserRole.class))).willReturn(token);

            AuthResponse response = authService.signup(request);

            verify(userRepository, times(1)).save(any(User.class));
            assertNotNull(response);
            assertEquals(token, response.getBearerToken());
        }
    }
}