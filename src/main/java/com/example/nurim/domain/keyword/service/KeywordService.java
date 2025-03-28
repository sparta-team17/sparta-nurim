package com.example.nurim.domain.keyword.service;

import com.example.nurim.domain.keyword.dto.request.KeywordResponseDto;
import com.example.nurim.domain.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional(readOnly = true)
    public List<KeywordResponseDto> findKeywords(int rankSize) {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);

        return keywordRepository.findByOrderBySearchCountDesc(oneDayAgo, rankSize).stream()
                .map(keyword -> new KeywordResponseDto(keyword.getSearchKeyword(), keyword.getSearchCount()))
                .toList();
    }

    @Transactional
    public void deleteOldKeywords() {
        keywordRepository.deleteAll();
    }
}
