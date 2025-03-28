package com.example.nurim.domain.program.scheduler;

import com.example.nurim.domain.program.repository.ProgramViewRepository;
import com.example.nurim.domain.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ProgramScheduler {

  private final ProgramService programService;
  private final ProgramViewRepository programViewRepository;

  @Scheduled(cron = "*/10 * * * * *") // 10초마다 실행
  @Transactional
  public void updateProgramStatusBySchedule() {
    programService.updateProgramStatus(LocalDateTime.now());

  }
  //레디스용
  @Scheduled(cron = "0 0 0 * * *") // 매일 자정
  @Transactional
  public void resetProgramViewCounts(){
    programService.resetProgramViewCounts();
  }
  // 디비용
  @Scheduled(cron = "0 0 0 * * *") // 매일 자정
  @Transactional
  public void resetProgramViewsDB() {
    // 중복 방지용
    programViewRepository.deleteAllViews();
    // 조회수 초기화
    programViewRepository.resetAllViewCounts();
  }
}
