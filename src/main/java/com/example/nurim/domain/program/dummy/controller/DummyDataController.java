package com.example.nurim.domain.program.dummy.controller;

import com.example.nurim.domain.program.dummy.service.DummyDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dummy")
@RequiredArgsConstructor
public class DummyDataController {

  private final DummyDataService dummyDataService;

  @PostMapping
  public ResponseEntity<String> insertDummyPrograms() {
    dummyDataService.insertDummyPrograms(50000); // 원하는 개수만큼 넣기
    return ResponseEntity.ok(" 5만건 삽입");
  }
}