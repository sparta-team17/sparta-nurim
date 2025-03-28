package com.example.nurim.domain.notice.repository;

import com.example.nurim.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice,Long>, NoticeRepositoryQuery {
    Optional <Notice> findByIdAndDeletedAtIsNull(Long noticeId);

    @Modifying
    @Query("UPDATE Notice n SET n.count = 0")
    void resetAllNoticeCount();
}
