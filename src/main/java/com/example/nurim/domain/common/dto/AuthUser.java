package com.example.nurim.domain.common.dto;

import com.example.nurim.domain.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class AuthUser {
    private final Long id;
    private final String email;
    private final String name;
    private final Collection<? extends GrantedAuthority> authorities;

    public static AuthUser of(Claims claims) {
        Long id = Long.valueOf(claims.getSubject());
        String email = claims.get("email", String.class);
        String name = claims.get("name", String.class);
        UserRole role = UserRole.valueOf(claims.get("role", String.class));
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.name()));
        return new AuthUser(id, email, name, authorities);
    }
}
