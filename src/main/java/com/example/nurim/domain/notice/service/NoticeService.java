package com.example.nurim.domain.notice.service;

import com.example.nurim.domain.common.exception.InvalidRequestException;
import com.example.nurim.domain.notice.dto.response.NoticeResponseDto;
import com.example.nurim.domain.notice.entity.Notice;
import com.example.nurim.domain.notice.repository.NoticeRepository;
import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    @Transactional
    public NoticeResponseDto createNotice(Long userId, String title, String contents) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("존재하지 않는 유저입니다."));

        Notice notice = new Notice(title, contents, user);
        noticeRepository.save(notice);
        return NoticeResponseDto.fromEntity(notice);
    }
}
