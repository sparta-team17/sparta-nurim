package com.example.nurim.domain.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
//import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class AuthUser {
    private final Long id;
    private final String email;
    private final String name;
//    private final Collection<? extends GrantedAuthority> authorities;
}
