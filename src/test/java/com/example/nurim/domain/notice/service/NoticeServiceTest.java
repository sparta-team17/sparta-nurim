package com.example.nurim.domain.notice.service;

import com.example.nurim.config.SchedulerConfig;
import com.example.nurim.domain.common.exception.CustomException;
import com.example.nurim.domain.common.exception.ErrorCode;
import com.example.nurim.domain.notice.dto.response.NoticeResponseDto;
import com.example.nurim.domain.notice.dto.response.NoticeSearchResponseDto;
import com.example.nurim.domain.notice.entity.Notice;
import com.example.nurim.domain.notice.entity.NoticeView;
import com.example.nurim.domain.notice.repository.NoticeRepository;
import com.example.nurim.domain.notice.repository.NoticeViewRepository;
import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Ref;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;
    @Mock
    private NoticeViewRepository noticeViewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SchedulerConfig schedulerConfig;
    @InjectMocks
    private NoticeService noticeService;

    @Nested
    class CreateNoticeTest {
        @Test
        void 공지사항_생성_성공() {
            Long userId = 1L;
            String email = "a@test.com";
            String password = "1234";
            String name = "이름";
            String title = "제목";
            String contents = "내용";

            User user = new User(email, password, name);

            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

            NoticeResponseDto responseDto = noticeService.createNotice(userId, title, contents);

            assertThat(responseDto).isNotNull();
            assertThat(responseDto.getTitle()).isEqualTo(title);
        }

        @Test
        void 공지사항_생성_유저조회_실패() {
            Long userId = 1L;
            String title = "제목";
            String contents = "내용";

            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            CustomException exception = assertThrows(CustomException.class, () -> {
                noticeService.createNotice(userId, title, contents);
            });

            assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        }
    }

    @Nested
    class UpdateNoticeTest {
        @Test
        void 공지사항_업데이트_성공() {
            Long userId = 1L;
            Long noticeId = 1L;
            String email = "a@test.com";
            String password = "1234";
            String name = "이름";
            String title = "제목";
            String contents = "내용";
            String updatedTitle = "수정된 제목";
            String updatedContents = null;

            User user = new User(email, password, name);
            ReflectionTestUtils.setField(user, "id", userId);

            Notice notice = new Notice();
            ReflectionTestUtils.setField(notice, "id", noticeId);
            ReflectionTestUtils.setField(notice, "user", user);
            ReflectionTestUtils.setField(notice, "title", title);
            ReflectionTestUtils.setField(notice, "contents", contents);

            given(noticeRepository.findByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.of(notice));

            NoticeResponseDto responseDto = noticeService.updateNotice(userId, noticeId, updatedTitle, updatedContents);

            assertThat(responseDto.getTitle()).isEqualTo(updatedTitle);
            assertThat(responseDto.getContents()).isEqualTo(contents);
        }

        @Test
        void 공지사항_업데이트_조회_실패() {
            Long userId = 1L;
            Long noticeId = 1L;
            String updatedTitle = "수정된 제목";
            String updatedContents = null;

            given(noticeRepository.findByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.empty());

            CustomException exception = assertThrows(CustomException.class, () -> {
                noticeService.updateNotice(userId, noticeId, updatedTitle, updatedContents);
            });

            assertEquals(ErrorCode.NOTICE_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        void 공지사항_업데이트_USER_ID_불일치() {
            Long userId = 1L;
            Long requestUserId = 2L;
            Long noticeId = 1L;
            String email = "a@test.com";
            String password = "1234";
            String name = "이름";
            String title = "제목";
            String contents = "내용";
            String updatedTitle = "수정된 제목";
            String updatedContents = null;

            User user = new User(email, password, name);
            ReflectionTestUtils.setField(user, "id", userId);

            Notice notice = new Notice();
            ReflectionTestUtils.setField(notice, "id", noticeId);
            ReflectionTestUtils.setField(notice, "user", user);
            ReflectionTestUtils.setField(notice, "title", title);
            ReflectionTestUtils.setField(notice, "contents", contents);

            given(noticeRepository.findByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.of(notice));

            CustomException exception = assertThrows(CustomException.class, () -> {
                noticeService.updateNotice(requestUserId, noticeId, updatedTitle, updatedContents);
            });

            assertEquals(ErrorCode.NOT_POST_OWNER, exception.getErrorCode());
        }
    }

    @Nested
    class deleteNoticeTset {
        @Test
        void 공지사항_삭제_성공() {
            Long userId = 1L;
            Long noticeId = 1L;
            String email = "a@test.com";
            String password = "1234";
            String name = "이름";

            User user = new User(email, password, name);
            ReflectionTestUtils.setField(user, "id", userId);

            Notice notice = new Notice();
            ReflectionTestUtils.setField(notice, "id", noticeId);
            ReflectionTestUtils.setField(notice, "user", user);

            given(noticeRepository.findByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.of(notice));

            NoticeResponseDto responseDto = noticeService.deleteNotice(userId, noticeId);

            assertThat(responseDto.getDeleteAt()).isNotNull();

        }

        @Test
        void 공지사항_삭제_조회_실패() {
            Long userId = 1L;
            Long noticeId = 1L;

            given(noticeRepository.findByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.empty());

            CustomException exception = assertThrows(CustomException.class, () -> {
                noticeService.deleteNotice(userId, noticeId);
            });

            assertEquals(ErrorCode.NOTICE_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        void 공지사항_삭제_USER_ID_불일치() {
            Long userId = 1L;
            Long requestUserId = 2L;
            Long noticeId = 1L;
            String email = "a@test.com";
            String password = "1234";
            String name = "이름";

            User user = new User(email, password, name);
            ReflectionTestUtils.setField(user, "id", userId);

            Notice notice = new Notice();
            ReflectionTestUtils.setField(notice, "id", noticeId);
            ReflectionTestUtils.setField(notice, "user", user);

            given(noticeRepository.findByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.of(notice));

            CustomException exception = assertThrows(CustomException.class, () -> {
                noticeService.deleteNotice(requestUserId, noticeId);
            });

            assertEquals(ErrorCode.NOT_POST_OWNER, exception.getErrorCode());
        }
    }


    @Nested
    class findNoticeTest{
        @Test
        void 공지사항_상세조회_성공() {
            Long userId = 1L;
            Long noticeId = 1L;
            String email = "a@test.com";
            String password = "1234";
            String name = "이름";
            String title = "제목";
            String contents = "내용";

            User user = new User(email, password, name);
            ReflectionTestUtils.setField(user, "id", userId);

            Notice notice = new Notice();
            ReflectionTestUtils.setField(notice, "id", noticeId);
            ReflectionTestUtils.setField(notice, "user", user);
            ReflectionTestUtils.setField(notice, "title", title);
            ReflectionTestUtils.setField(notice, "contents", contents);

            given(noticeRepository.findByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.of(notice));

            NoticeResponseDto responseDto = noticeService.findNoticeWithDb(noticeId, userId);

            assertThat(responseDto).isNotNull();
            assertThat(responseDto.getNoticeId()).isEqualTo(noticeId);

        }
        @Test
        void 공지사항_상세조회_실패(){
            Long noticeId = 1L;
            Long userId = 1L;

            given(noticeRepository.findByIdAndDeletedAtIsNull(anyLong())).willReturn(Optional.empty());

            CustomException exception = assertThrows(CustomException.class, () -> {
                noticeService.findNoticeWithDb(noticeId, userId);
            });

            assertEquals(ErrorCode.NOTICE_NOT_FOUND, exception.getErrorCode());
        }
        @Test
        void 공지사항_스케줄러_실행후_조회수_초기화_검증() {
            // given
            Long userId = 1L;
            String email = "a@test.com";
            String password = "1234";
            String name = "이름";
            User user = new User(email,password,name);

            Long noticeId = 1L;
            Integer count = 0;
            Notice notice = new Notice();
            ReflectionTestUtils.setField(notice,"id", noticeId);
            ReflectionTestUtils.setField(notice, "count", count);
            ReflectionTestUtils.setField(notice,"user",user);

            given(noticeRepository.findByIdAndDeletedAtIsNull(noticeId)).willReturn(Optional.of(notice));
            given(noticeViewRepository.existsByUserIdAndNoticeId(userId, noticeId)).willReturn(false);

            // 첫 조회
            // 조회수 초기화
            NoticeResponseDto firstResponse = noticeService.findNoticeWithDb(noticeId, userId);
            assertEquals(1, firstResponse.getCount()); // 조회수 1 확인

            // 스케줄러 실행 (조회 기록 초기화)
            schedulerConfig.clearNoticeViews();
            // 초기화 후 두번째 조회
            NoticeResponseDto secondResponse = noticeService.findNoticeWithDb(noticeId, userId);

            // then
            assertEquals(2, secondResponse.getCount()); // 조회수 2 확인
        }
    }

    @Nested
    class findNoticesTest{
        @Test
        void 공지사항_검색_조회_성공() {
            // given
            int page = 1;
            int size = 5;
            String keyword = "공지";
            String userName = "관리자";
            Integer count = 123;
            LocalDateTime createdAt = LocalDateTime.now();
            Pageable pageable = PageRequest.of(page - 1, size);

            List<NoticeSearchResponseDto> noticeList = List.of(
                    new NoticeSearchResponseDto(1L, "공지사항1",count, createdAt,userName),
                    new NoticeSearchResponseDto(2L, "공지사항2",count, createdAt,userName)
            );
            Page<NoticeSearchResponseDto> mockPage = new PageImpl<>(noticeList, pageable, noticeList.size());

            given(noticeRepository.findNotices(keyword, pageable)).willReturn(mockPage);

            // when
            Page<NoticeSearchResponseDto> result = noticeService.findNotices(page, size, keyword);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getContent().get(0).getTitle()).isEqualTo("공지사항1");
            assertThat(result.getTotalElements()).isEqualTo(2);

        }
    }
}