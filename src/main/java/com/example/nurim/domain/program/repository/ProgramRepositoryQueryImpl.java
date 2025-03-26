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
  public Page<ProgramListRequestDto> findProgramList(String title, String location, ProgramStatus status, LocalDateTime usageDate, Pageable pageable) {
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
        .from(program,programDate)
        .leftJoin(program.category, category)
        .where( //제목, 장소, 상태 , 접수 일정 조건
            title != null ? program.title.containsIgnoreCase(title) : null,
            location != null ? program.location.containsIgnoreCase(location) : null,
            status != null ? program.status.eq(status) : null,
            usageDate != null ? programDate.program.id.eq(program.id)
                .and(programDate.date.year().eq(usageDate.getYear()))
                .and(programDate.date.month().eq(usageDate.getMonthValue()))
                .and(programDate.date.dayOfMonth().eq(usageDate.getDayOfMonth())) : null,
            program.deletedAt.isNull()
        )
        .orderBy(program.createdAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
    // 전체 개수 조회
    long total = Optional.ofNullable(jpaQueryFactory
        .select(program.count())
        .from(program, programDate)
        .where(
            title != null ? program.title.containsIgnoreCase(title) : null,
            location != null ? program.location.containsIgnoreCase(location) : null,
            status != null ? program.status.eq(status) : null,
            usageDate != null ? programDate.program.id.eq(program.id)
                .and(programDate.date.year().eq(usageDate.getYear()))
                .and(programDate.date.month().eq(usageDate.getMonthValue()))
                .and(programDate.date.dayOfMonth().eq(usageDate.getDayOfMonth())) : null,
            program.deletedAt.isNull()
        )
        .fetchOne()).orElse(0L);

    return PageableExecutionUtils.getPage(content, pageable, () -> total);
  }
}