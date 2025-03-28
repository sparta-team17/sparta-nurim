package com.example.nurim.domain.user.repository;

import com.example.nurim.domain.common.exception.CustomException;
import com.example.nurim.domain.common.exception.ErrorCode;
import com.example.nurim.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmailAndDeletedAtIsNull(String email);
    Boolean existsByEmailAndDeletedAtIsNotNull(String email);
    Optional<User> findByEmailAndDeletedAtIsNull(String email);
    Optional<User> findByIdAndDeletedAtIsNull(Long id);
    default User findActiveByIdOrElseThrow(Long id) {
        return findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
