package com.example.nurim.domain.auth.entity;

import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.enums.UserRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserInfo {
    private Long id;
    private String email;
    private String name;
    private UserRole role;

    public static UserInfo of(User user) {
        return new UserInfo(user.getId(), user.getEmail(), user.getName(), user.getRole());
    }
}
