package com.example.nurim.domain.notice.repository;

import com.example.nurim.domain.notice.dto.response.NoticeResponseDto;

import com.example.nurim.domain.notice.dto.response.NoticeSearchResponseDto;
import com.example.nurim.domain.notice.dto.response.QNoticeSearchResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.nurim.domain.notice.entity.QNotice.notice;
import static com.example.nurim.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class NoticeRepositoryQueryImpl implements NoticeRepositoryQuery{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<NoticeSearchResponseDto> findNotices(String keyword, Pageable pageable) {
        List<NoticeSearchResponseDto> results = jpaQueryFactory
                .select(new QNoticeSearchResponseDto(
                        notice.id,
                        notice.title,
                        notice.createdAt,
                        user.name
                ))
                .from(notice)
                .leftJoin(user).on(user.eq(notice.user))
                .where(
                        titleContains(keyword)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(notice.id)
                .orderBy(notice.createdAt.desc())
                .fetch();

        Long totalCount = jpaQueryFactory
                .select(Wildcard.count)
                .from(notice)
                .where(
                        titleContains(keyword)
                ).fetchOne();

        return new PageImpl<>(results, pageable, totalCount != null ? totalCount : 0L);
    }

    private BooleanExpression titleContains(String title) {
        return title != null ? notice.title.containsIgnoreCase(title) : null;
    }
}
