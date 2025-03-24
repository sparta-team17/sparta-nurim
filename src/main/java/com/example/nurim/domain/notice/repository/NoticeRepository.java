package com.example.nurim.domain.notice.repository;

import com.example.nurim.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long> {
}
