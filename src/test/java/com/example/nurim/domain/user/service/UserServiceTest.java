package com.example.nurim.domain.user.service;

import com.example.nurim.domain.user.dto.request.UpdateNameRequest;
import com.example.nurim.domain.user.dto.request.UpdatePasswordRequest;
import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.exception.UserException;
import com.example.nurim.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UpdateNameTests {
        @Test
        @Order(1)
        void 사용자_이름_수정_성공() {
            UpdateNameRequest request = new UpdateNameRequest("newName");
            User user = new User("temp@gmail.com", "encodedPassword", "name");

            given(userRepository.findActiveByIdOrElseThrow(anyLong())).willReturn(user);

            userService.updateName(1L, request);

            assertEquals(request.getName(), user.getName());
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UpdatePasswordTests {

        private final User user = new User("temp@gmail.com", "encodedPassword", "name");

        @Test
        @Order(1)
        void 비밀번호_수정_현재비밀번호_불일치_실패() {
            UpdatePasswordRequest request = new UpdatePasswordRequest("oldPassword", "newPassword");

            given(userRepository.findActiveByIdOrElseThrow(anyLong())).willReturn(user);
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

            UserException thrown = assertThrows(UserException.class, () -> userService.updatePassword(1L, request));
            assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
            assertEquals("Current password is incorrect", thrown.getMessage());
        }

        @Test
        @Order(2)
        void 비밀번호_수정_새비밀번호_현재비밀번호_일치_실패() {
            UpdatePasswordRequest request = new UpdatePasswordRequest("samePassword", "samePassword");

            given(userRepository.findActiveByIdOrElseThrow(anyLong())).willReturn(user);
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

            UserException thrown = assertThrows(UserException.class, () -> userService.updatePassword(1L, request));
            assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
            assertEquals("New password cannot be the same as the current password", thrown.getMessage());
        }

        @Test
        @Order(3)
        void 비밀번호_수정_성공() {
            String newEncodedPassword = "newEncodedPassword";
            UpdatePasswordRequest request = new UpdatePasswordRequest("oldPassword", "newPassword");

            given(userRepository.findActiveByIdOrElseThrow(anyLong())).willReturn(user);
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
            given(passwordEncoder.encode(anyString())).willReturn(newEncodedPassword);

            userService.updatePassword(1L, request);

            assertEquals(newEncodedPassword, user.getPassword());
        }
    }
}