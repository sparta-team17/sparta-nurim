package com.example.nurim.domain.user.controller;

import com.example.nurim.domain.common.dto.AuthUser;
import com.example.nurim.domain.user.dto.request.UpdateNameRequest;
import com.example.nurim.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/name")
    public void updateName(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UpdateNameRequest request
    ) {
        userService.updateName(authUser.getId(), request);
    }
}
