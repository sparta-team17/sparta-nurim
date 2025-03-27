package com.example.nurim.domain.notice.repository;

import com.example.nurim.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice,Long>, NoticeRepositoryQuery {
    Optional <Notice> findByIdAndDeletedAtIsNull(Long noticeId);
}
