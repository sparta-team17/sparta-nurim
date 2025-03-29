package com.example.nurim.domain.notice.scheduler;

import com.example.nurim.domain.notice.repository.NoticeRepository;
import com.example.nurim.domain.notice.repository.NoticeViewRepository;
import com.example.nurim.domain.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NoticeScheduler {

    private final NoticeService noticeService;

    @Scheduled(cron = "0 0 0 * * *")
    public void runNoticeviewScheduler(){
        try {
            log.info("공지사항 조회 내역 초기화 시작");
            noticeService.clearNoticeViews();
            noticeService.resetAllNoticeCount();
            log.info("공지사항 조회 기록 초기화 완료");
        } catch (Exception e) {
            log.error("스케줄러 실행 중 오류 발생", e);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void runNoticeRedisScheduler(){
        try{
            log.info("Redis 공지사항 조회 내역 초기화 시작");
            noticeService.clearViewCountAndViews();
            log.info("Redis 공지사항 조회 기록 초기화 완료");
        }catch (Exception e){
            log.error("스케쥴러 실행 중 오류 발생", e);
        }
    }
}

