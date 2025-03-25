package com.example.nurim.domain.notice.service;

import com.example.nurim.domain.common.exception.InvalidRequestException;
import com.example.nurim.domain.common.exception.UnauthorizedException;
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

    @Transactional
    public NoticeResponseDto updateNotice(Long userId, Long noticeId, String title, String contents) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(()-> new InvalidRequestException("존재하지 않는 공지사항입니다."));

        if(!notice.getUser().getId().equals(userId)){
            throw new UnauthorizedException("작성자만 수정할 수 있습니다.");
        }

        notice.updateNotice(title, contents);
        noticeRepository.flush();
        return NoticeResponseDto.fromEntity(notice);
    }
}
