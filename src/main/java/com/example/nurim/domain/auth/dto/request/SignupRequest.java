package com.example.nurim.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequest {
    @Email
    @NotBlank
    private final String email;
    @NotBlank
    private final String password;
    @NotBlank
    private final String name;
}
