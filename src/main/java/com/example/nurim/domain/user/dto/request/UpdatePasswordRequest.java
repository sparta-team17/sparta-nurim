package com.example.nurim.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdatePasswordRequest {
    @NotBlank
    private final String oldPassword;
    @NotBlank
    private final String newPassword;
}
