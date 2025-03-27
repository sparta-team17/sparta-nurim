package com.example.nurim.domain.notice.repository;

import com.example.nurim.domain.notice.entity.NoticeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface NoticeViewRepository extends JpaRepository<NoticeView, Long> {
    boolean existsByUserIdAndNoticeId(Long userId, Long noticeId);

    @Transactional
    @Modifying
    @Query("DELETE FROM NoticeView")
    void deleteAllViews();
}
