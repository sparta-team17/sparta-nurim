package com.example.nurim.domain.program.scheduler;

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

  @Scheduled(cron = "10 * * * * *") // 매분 10초마다 실행
  @Transactional
  public void updateProgramStatusBySchedule() {
    programService.updateProgramStatus(LocalDateTime.now());

  }

}
