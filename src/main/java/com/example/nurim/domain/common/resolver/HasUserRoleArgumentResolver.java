package com.example.nurim.domain.common.resolver;

import com.example.nurim.domain.common.annotation.HasUserRole;
import com.example.nurim.domain.common.dto.AuthUser;
import com.example.nurim.domain.user.enums.UserRole;
import org.springframework.core.MethodParameter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class HasUserRoleArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String ROLE_USER = UserRole.ROLE_USER.name();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.getParameterAnnotation(HasUserRole.class) != null;
        boolean isAssignableType = parameter.getParameterType().equals(AuthUser.class);
        return hasAnnotation && isAssignableType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        if (authUser == null) {
            return null;
        }

        boolean hasUserRole = authUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(ROLE_USER));
        if (!hasUserRole) {
            throw new AccessDeniedException("You do not have the role '" + ROLE_USER + "'");
        }

        return authUser;
    }
}
