package com.example.nurim.domain.user.controller;

import com.example.nurim.domain.common.dto.AuthUser;
import com.example.nurim.domain.review.dto.response.UserReviewResponse;
import com.example.nurim.domain.user.dto.request.UpdateNameRequest;
import com.example.nurim.domain.user.dto.request.UpdatePasswordRequest;
import com.example.nurim.domain.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private static final String FIRST_PAGE = "1";
    private static final String DEFAULT_SIZE = "10";
    private static final String ROLE_USER = "ROLE_USER";

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

    @Secured(ROLE_USER)
    @GetMapping("/reviews")
    public Page<UserReviewResponse> findReviews(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(defaultValue = FIRST_PAGE) @Min(1) Integer page,
            @RequestParam(defaultValue = DEFAULT_SIZE) @Min(1) Integer size
    ) {
        return userService.findReviews(authUser.getId(), page, size);
    }
}
