package com.example.nurim.domain.notice.service;

import com.example.nurim.domain.notice.repository.NoticeRepository;
import com.example.nurim.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private NoticeService noticeService;

    @Nested
    class CreateNoticeTest{
        @Test
        void 공지사항_생성_성공() {
        }
        @Test
        void 공지사항_생성_유저조회_실패(){

        }
    }

    @Test
    void updateNotice() {
    }

    @Test
    void deleteNotice() {
    }

    @Test
    void findNotice() {
    }

    @Test
    void findNotices() {
    }
}