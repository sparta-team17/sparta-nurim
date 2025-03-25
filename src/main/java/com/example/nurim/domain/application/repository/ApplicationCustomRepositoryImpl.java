package com.example.nurim.domain.application.repository;

import com.example.nurim.domain.application.dto.response.UserApplicationResponse;
import com.example.nurim.domain.application.enums.ApplicationStatus;
import com.example.nurim.domain.user.dto.request.FindApplicationRequest;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.nurim.domain.application.entity.QApplication.application;
import static com.example.nurim.domain.program.entity.QProgram.program;
import static com.example.nurim.domain.program.entity.QProgramDate.programDate;

@RequiredArgsConstructor
public class ApplicationCustomRepositoryImpl implements ApplicationCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserApplicationResponse> findByUserId(Long userId, FindApplicationRequest request, Pageable pageable) {
        List<UserApplicationResponse> applicationList = queryFactory
                .select(Projections.constructor(UserApplicationResponse.class,
                        application.id,
                        application.createdAt,
                        programDate.date,
                        program.id,
                        program.title,
                        program.deletedAt
                ))
                .from(application)
                .join(application.programDate, programDate)
                .join(programDate.program, program)
                .where(application.user.id.eq(userId),
                        hasStatus(request.getStatus()),
                        createdAtAfterOrOn(request.getCreatedFrom()),
                        createdBeforeOrOn(request.getCreatedTo()),
                        titleContains(request.getTitle()))
                .orderBy(application.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(Wildcard.count)
                .from(application)
                .join(application.programDate, programDate)
                .join(programDate.program, program)
                .where(application.user.id.eq(userId),
                        hasStatus(request.getStatus()),
                        createdAtAfterOrOn(request.getCreatedFrom()),
                        createdBeforeOrOn(request.getCreatedTo()),
                        titleContains(request.getTitle()))
                .fetchOne();

        return new PageImpl<>(applicationList, pageable, count);
    }

    private BooleanExpression hasStatus(ApplicationStatus status) {
        return status != null ? application.status.eq(status) : null;
    }

    private BooleanExpression createdAtAfterOrOn(LocalDateTime createdFrom) {
        return createdFrom != null ? application.createdAt.goe(createdFrom) : null;
    }

    private BooleanExpression createdBeforeOrOn(LocalDateTime createdTo) {
        return createdTo != null ? application.createdAt.loe(createdTo) : null;
    }

    private BooleanExpression titleContains(String title) {
        return title != null ? program.title.contains(title) : null;
    }
}
