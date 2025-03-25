package com.example.nurim.domain.program.entity;

import com.example.nurim.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "program_date")
public class ProgramDate extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    private Integer count;

    private LocalDateTime date;

    public ProgramDate(Program program, LocalDateTime date) {
        this.program = program;
        this.date = date;
        this.count = 0; // 초기 신청자 수를 0으로 설정
    }
}
