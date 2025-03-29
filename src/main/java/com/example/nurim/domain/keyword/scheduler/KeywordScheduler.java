package com.example.nurim.domain.keyword.scheduler;

import com.example.nurim.domain.keyword.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KeywordScheduler {

    private final KeywordService keywordService;
    private final CacheManager cacheManager;

    @Scheduled(cron = "5 * * * * *")
    @Transactional
    public void deleteOldKeywordSchedule() {
        keywordService.deleteOldKeywords();
        deleteCache("findProgramList");
    }

    private void deleteCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }
}
