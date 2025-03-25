package com.example.nurim.domain.user.repository;

import com.example.nurim.domain.user.entity.User;
import com.example.nurim.domain.user.exception.UserException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmailAndDeletedAtIsNull(String email);
    Boolean existsByEmailAndDeletedAtIsNotNull(String email);
    Optional<User> findByEmailAndDeletedAtIsNull(String email);
    Optional<User> findByIdAndDeletedAtIsNull(Long id);
    default User findActiveByIdOrElseThrow(Long id) {
        return findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
