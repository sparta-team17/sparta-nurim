//package com.example.nurim.domain.program.service;
//
//import com.example.nurim.domain.program.entity.Category;
//import com.example.nurim.domain.program.repository.CategoryRepository;
//import com.example.nurim.domain.program.repository.ProgramDateRepository;
//import com.example.nurim.domain.program.repository.ProgramRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import javax.swing.text.html.Option;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//class ProgramServiceTest {
//  @Mock
//  private ProgramRepository programRepository;
//
//  @Mock
//  private CategoryRepository categoryRepository;
//
//  @Mock
//  private ProgramDateRepository programDateRepository;
//
//  @InjectMocks
//  private ProgramService programService;
//
//  @Test
//  void Program을_생성할_수_있다() {
//    //given
//    Category category = new Category("스파르타");
//    when(categoryRepository.findById(1l)).thenReturn(Optional.of(category))
//
//  }
//
////  @Test
//  void findAll() {
//  }
//
//  @Test
//  void findById() {
//  }
//
//  @Test
//  void updateProgram() {
//  }
//
//  @Test
//  void updateStatus() {
//  }
//
//  @Test
//  void deleteProgram() {
//  }
//}