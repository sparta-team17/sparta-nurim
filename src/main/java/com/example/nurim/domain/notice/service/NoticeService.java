package com.example.nurim.domain.notice.service;

import com.example.nurim.domain.common.exception.CustomException;
import com.example.nurim.domain.common.exception.ErrorCode;
import com.example.nurim.domain.notice.dto.response.NoticeResponseDto;
import com.example.nurim.domain.notice.dto.response.NoticeSearchResponseDto;
import com.example.nurim.domain.notice.entity.Notice;
import com.example.nurim.domain.notice.repository.NoticeRepository;
import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    private RedisTemplate<String, Integer> redisTemplate;

    @Transactional
    public NoticeResponseDto createNotice(Long userId, String title, String contents) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Notice notice = new Notice(title, contents, user);
        noticeRepository.save(notice);
        return NoticeResponseDto.fromEntity(notice);
    }

    @Transactional
    public NoticeResponseDto updateNotice(Long userId, Long noticeId, String title, String contents) {
        Notice notice = noticeRepository.findByIdAndDeletedAtIsNull(noticeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTICE_NOT_FOUND));

        if (!notice.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_POST_OWNER);
        }

        notice.updateNotice(title, contents);
        noticeRepository.flush();
        return NoticeResponseDto.fromEntity(notice);
    }

    @Transactional
    public NoticeResponseDto deleteNotice(Long userId, Long noticeId) {
        Notice notice = noticeRepository.findByIdAndDeletedAtIsNull(noticeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTICE_NOT_FOUND));
        if (!notice.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_POST_OWNER);
        }
        notice.setDeletedAt(LocalDateTime.now());
        return NoticeResponseDto.fromEntity(notice);
    }

    public NoticeResponseDto findNotice(Long noticeId) {
        Notice notice = noticeRepository.findByIdAndDeletedAtIsNull(noticeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTICE_NOT_FOUND));
        return NoticeResponseDto.fromEntity(notice);
    }

    public Page<NoticeSearchResponseDto> findNotices(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return noticeRepository.findNotices(keyword, pageable);
    }
}
