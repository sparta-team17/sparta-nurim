package com.example.nurim.domain.auth.service;

import com.example.nurim.config.JwtUtil;
import com.example.nurim.domain.auth.dto.request.SigninRequest;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

            given(userRepository.existsByEmailAndDeletedAtIsNull(anyString())).willReturn(false);
            given(userRepository.existsByEmailAndDeletedAtIsNotNull(anyString())).willReturn(false);
            given(passwordEncoder.encode(anyString())).willReturn(encodedPassword);

            authService.signup(request);

            verify(userRepository, times(1)).save(any(User.class));
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SignInTests {

        private final SigninRequest request = new SigninRequest("temp@gmail.com", "password");

        @Test
        @Order(1)
        void 로그인_존재하지_않는_이메일이면_실패() {
            given(userRepository.findByEmailAndDeletedAtIsNull(anyString())).willReturn(Optional.empty());

            AuthException thrown = assertThrows(AuthException.class, () -> authService.signin(request));
            assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
            assertEquals("No account found with this email", thrown.getMessage());
        }

        @Test
        @Order(2)
        void 로그인_비밀번호가_일치하지_않으면_실패() {
            String encodedPassword = "encodedPassword";

            User user = new User(request.getEmail(), encodedPassword, "name");

            given(userRepository.findByEmailAndDeletedAtIsNull(anyString())).willReturn(Optional.of(user));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

            AuthException thrown = assertThrows(AuthException.class, () -> authService.signin(request));
            assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
            assertEquals("Invalid password", thrown.getMessage());
        }

        @Test
        @Order(3)
        void 로그인_성공() {
            String encodedPassword = "encodedPassword";
            String token = "testToken";

            User user = new User(request.getEmail(), encodedPassword, "name");

            given(userRepository.findByEmailAndDeletedAtIsNull(anyString())).willReturn(Optional.of(user));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
            given(jwtUtil.createToken(any(), anyString(), anyString(), any(UserRole.class))).willReturn(token);

            AuthResponse response = authService.signin(request);

            assertNotNull(response);
            assertEquals(token, response.getBearerToken());
        }
    }
}