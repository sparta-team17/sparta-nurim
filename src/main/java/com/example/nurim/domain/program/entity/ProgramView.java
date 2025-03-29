package com.example.nurim.domain.program.entity;

import com.example.nurim.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "program_views")
@NoArgsConstructor
public class ProgramView extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long programId;

  @Column(nullable = false)
  private Long userId;

  public ProgramView(Long programId, Long userId) {
    this.programId = programId;
    this.userId = userId;
  }
}