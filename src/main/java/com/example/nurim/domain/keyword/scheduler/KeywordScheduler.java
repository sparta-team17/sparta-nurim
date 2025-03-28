package com.example.nurim.domain.keyword.scheduler;

import com.example.nurim.domain.keyword.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KeywordScheduler {

    private final KeywordService keywordService;

    @Scheduled(cron = "5 * * * * *")
    @Transactional
    public void deleteOldKeywordSchedule() {
        keywordService.deleteOldKeywords();
    }
}
