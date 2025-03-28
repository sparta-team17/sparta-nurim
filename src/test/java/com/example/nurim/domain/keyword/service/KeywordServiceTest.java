package com.example.nurim.domain.keyword.service;

import com.example.nurim.domain.keyword.dto.request.KeywordResponseDto;
import com.example.nurim.domain.keyword.entity.Keyword;
import com.example.nurim.domain.keyword.repository.KeywordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KeywordServiceTest {

    @Mock
    private KeywordRepository keywordRepository;
    @InjectMocks
    private KeywordService keywordService;

    @Test
    void 인기_검색어_조회_성공() {
        // Given
        int rankSize = 2;

        Keyword keyword1 = new Keyword("프로그램", 2L);
        Keyword keyword2 = new Keyword("만들기", 1L);

        List<Keyword> keywordList = List.of(keyword1, keyword2);

        given(keywordRepository.findByOrderBySearchCountDesc(any(), anyInt())).willReturn(keywordList);

        // When
        List<KeywordResponseDto> keywords = keywordService.findKeywords(rankSize);

        // Then
        assertThat(keywords).isNotNull();
        assertThat(keywords.get(0).getKeyword()).isEqualTo("프로그램");
        assertThat(keywords.size()).isEqualTo(2);
    }

    @Test
    void 인기_검색어_삭제() {
        // When
        keywordService.deleteOldKeywords();

        // Then
        verify(keywordRepository, times(1)).deleteAll();
    }
  
}