package com.example.nurim.domain.auth.service;

import com.example.nurim.domain.auth.entity.RefreshToken;
import com.example.nurim.domain.auth.entity.UserInfo;
import com.example.nurim.domain.auth.repository.RefreshTokenRepository;
import com.example.nurim.domain.common.exception.CustomException;
import com.example.nurim.domain.common.exception.ErrorCode;
import com.example.nurim.domain.user.entity.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(refreshTokenService, "expiration", 86400000L);
    }

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CreateRefreshTokenTests {

        @Test
        @Order(1)
        void refresh_token_발급_성공() {
            UserInfo userInfo = UserInfo.of(new User("temp@gmail.com", "password", "name"));

            String newToken = refreshTokenService.createRefreshToken(userInfo);

            verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
            assertNotNull(newToken);
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ExtractUserInfoTests {

        @Test
        @Order(1)
        void 토큰에서_사용자_정보_추출_저장소에_토큰_없음_실패() {
            String token = "refreshToken";
            given(refreshTokenRepository.findById(anyString())).willReturn(Optional.empty());

            CustomException thrown = assertThrows(CustomException.class, () -> refreshTokenService.extractUserInfo(token));

            assertEquals(ErrorCode.INVALID_REFRESH_TOKEN, thrown.getErrorCode());
        }

        @Test
        @Order(2)
        void 토큰에서_사용자_정보_추출_성공() {
            String token = "refreshToken";
            RefreshToken refreshToken = new RefreshToken(token, 1000L, UserInfo.of(new User("temp@gmail.com", "password", "name")));

            given(refreshTokenRepository.findById(anyString())).willReturn(Optional.of(refreshToken));

            UserInfo userInfo = refreshTokenService.extractUserInfo(token);

            assertNotNull(userInfo);
            assertEquals(refreshToken.getUserInfo().getEmail(), userInfo.getEmail());
        }
    }
}