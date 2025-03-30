package com.example.nurim.domain.program.dummy.service;

import com.example.nurim.domain.common.exception.CustomException;
import com.example.nurim.domain.common.exception.ErrorCode;
import com.example.nurim.domain.program.entity.Category;
import com.example.nurim.domain.program.entity.Program;
import com.example.nurim.domain.program.entity.ProgramDate;
import com.example.nurim.domain.program.enums.ProgramDateStatus;
import com.example.nurim.domain.program.enums.ProgramStatus;
import com.example.nurim.domain.program.repository.CategoryRepository;
import com.example.nurim.domain.program.repository.ProgramDateRepository;
import com.example.nurim.domain.program.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DummyDataService {

  private final ProgramRepository programRepository;
  private final ProgramDateRepository programDateRepository;
  private final CategoryRepository categoryRepository;

  @Transactional
  public void insertDummyPrograms(int count) {
    Category category = categoryRepository.findById(1L)
        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

    String[] locations = {"서울", "부산", "대구", "인천", "광주", "대전", "울산", "제주", "춘천"};

    for (int i = 1; i <= count; i++) {
      String title = "프로그램" + i;
      String location = locations[i % locations.length];
      ProgramStatus status = (i % 3 == 0) ? ProgramStatus.ACCEPTING : ProgramStatus.COMPLETE;

      Program program = new Program();
      program.setTitle(title);
      program.setLocation(location);
      program.setStatus(status);
      program.setQuota(10L);
      program.setDetail("더미 프로그램입니다.");
      program.setPhone("010-0000-0000");
      program.setRegistrationStartDate(LocalDateTime.now());
      program.setRegistrationEndDate(LocalDateTime.now().plusDays(30));
      program.setCategory(category);


      Program savedProgram = programRepository.save(program);


      ProgramDate date = new ProgramDate();
      date.setProgram(savedProgram);
      date.setDate(LocalDateTime.now());
      date.setStatus(ProgramDateStatus.RECRUITING);
      date.setCount(0);


      programDateRepository.save(date);

      }
    }
  }


