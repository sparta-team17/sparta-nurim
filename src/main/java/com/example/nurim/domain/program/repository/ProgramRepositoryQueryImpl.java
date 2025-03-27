package com.example.nurim.domain.program.repository;

import com.example.nurim.domain.program.dto.responseDto.ProgramListRequestDto;
import com.example.nurim.domain.program.entity.QCategory;
import com.example.nurim.domain.program.entity.QProgram;
import com.example.nurim.domain.program.entity.QProgramDate;
import com.example.nurim.domain.program.enums.ProgramStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProgramRepositoryQueryImpl implements ProgramRepositoryQuery{
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<ProgramListRequestDto> findProgramList(String title, String location, ProgramStatus status,  Pageable pageable) {
    QProgram program = QProgram.program;
    QProgramDate programDate = QProgramDate.programDate;
    QCategory category = QCategory.category;

    List<ProgramListRequestDto> content = jpaQueryFactory
        .select(Projections.constructor(
            ProgramListRequestDto.class,
            program.id,
            program.title,
            program.location,
            category.name,
            program.status,
            program.quota,
            program.registrationStartDate,
            program.registrationEndDate
        ))
        .from(program)
        .leftJoin(program.category, category)
        .distinct() // 중복 제거
        .where(
            title != null ? program.title.containsIgnoreCase(title) : null,
            location != null ? program.location.containsIgnoreCase(location) : null,
            status != null ? program.status.eq(status) : null,
            program.deletedAt.isNull()
        )
        .orderBy(program.registrationStartDate.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    long total = Optional.ofNullable(jpaQueryFactory
        .select(program.countDistinct())
        .from(program)
        .leftJoin(program.category, category)
        .where(
            title != null ? program.title.containsIgnoreCase(title) : null,
            location != null ? program.location.containsIgnoreCase(location) : null,
            status != null ? program.status.eq(status) : null,
            program.deletedAt.isNull()
        )
        .fetchOne()).orElse(0L);

    return PageableExecutionUtils.getPage(content, pageable, () -> total);
  }
}