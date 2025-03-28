package com.example.nurim.domain.keyword.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeywordResponseDto {

    private String keyword;
    private Long count;
}
