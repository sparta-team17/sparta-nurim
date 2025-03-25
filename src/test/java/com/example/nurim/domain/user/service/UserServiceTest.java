package com.example.nurim.domain.user.service;

import com.example.nurim.domain.user.dto.request.UpdateNameRequest;
import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
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
}