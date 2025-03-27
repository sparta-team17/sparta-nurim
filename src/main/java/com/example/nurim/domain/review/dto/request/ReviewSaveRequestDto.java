package com.example.nurim.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSaveRequestDto {

    @NotNull(message = "후기 별점은 필수 값입니다.")
    @Min(0)
    @Max(5)
    private double rating;

    @NotBlank(message = "후기 내용은 필수 값입니다.")
    private String contents;
}
