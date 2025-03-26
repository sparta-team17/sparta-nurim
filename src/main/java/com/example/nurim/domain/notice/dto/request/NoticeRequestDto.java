package com.example.nurim.domain.notice.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NoticeRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String contents;
}
