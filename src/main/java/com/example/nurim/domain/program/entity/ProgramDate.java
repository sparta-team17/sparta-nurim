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

    // 신청자 수 증가 메서드 추가
    public void incrementCount() {
        this.count++;
    }

    // 신청 취소 시 count 감소
    public void decrementCount() {
        if (this.count > 0) {
            this.count--;
        }
    }
}
