package com.example.nurim.domain.user.controller;

import com.example.nurim.domain.common.dto.AuthUser;
import com.example.nurim.domain.user.dto.request.UpdateNameRequest;
import com.example.nurim.domain.user.dto.request.UpdatePasswordRequest;
import com.example.nurim.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/password")
    public void updatePassword(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UpdatePasswordRequest request
    ) {
        userService.updatePassword(authUser.getId(), request);
    }

    @DeleteMapping
    public void deleteUser(@AuthenticationPrincipal AuthUser authUser) {
        userService.deleteUser(authUser.getId());
    }
}
