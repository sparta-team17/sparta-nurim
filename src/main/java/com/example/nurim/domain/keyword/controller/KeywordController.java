package com.example.nurim.domain.keyword.controller;

import com.example.nurim.domain.keyword.dto.request.KeywordResponseDto;
import com.example.nurim.domain.keyword.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

    @GetMapping("/search/trending")
    public ResponseEntity<List<KeywordResponseDto>> findKeywords(
            @RequestParam(defaultValue = "3") int rankSize
    ) {
        return ResponseEntity.ok(keywordService.findKeywords(rankSize));
    }
}
