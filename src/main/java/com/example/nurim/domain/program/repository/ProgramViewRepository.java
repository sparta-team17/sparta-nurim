package com.example.nurim.domain.program.repository;

import com.example.nurim.domain.program.entity.ProgramView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface ProgramViewRepository extends JpaRepository<ProgramView, Long> {
  // 특정 사용자가 특정 프로그램을 조회한 적이 있는지 여부 확인
  boolean existsByUserIdAndProgramId(Long userId, Long programId);
  // 테이블의 모든 조회 기록 삭제
  @Transactional
  @Modifying
  @Query("DELETE FROM ProgramView")
  void deleteAllViews();
  // 모든 프로그램의 viewCount 값을 0으로 초기화
  @Modifying
  @Transactional
  @Query("UPDATE Program p SET p.viewCount = 0")
  void resetAllViewCounts();


}