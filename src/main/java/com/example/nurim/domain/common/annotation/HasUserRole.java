package com.example.nurim.domain.common.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize("hasRole(T(com.example.nurim.domain.user.enums.UserRole).ROLE_USER.name())")
public @interface HasUserRole {
}
