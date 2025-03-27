package com.example.nurim.domain.application.entity;

import com.example.nurim.domain.application.enums.ApplicationStatus;
import com.example.nurim.domain.common.entity.Timestamped;
import com.example.nurim.domain.program.entity.Program;
import com.example.nurim.domain.program.entity.ProgramDate;
import com.example.nurim.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "applications")
public class Application extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @Column(name = "appication_date", nullable = false)
    private LocalDateTime applicationDate; // 신청일

    @Column(name = "user_date", nullable = false)
    private LocalDateTime userDate; // 이용일

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_date_id", nullable = false)
    private ProgramDate programDate;

    public Application(Program program, LocalDateTime applicationDate, LocalDateTime userDate, ApplicationStatus status, User user, ProgramDate programDate) {
        this.program = program;
        this.applicationDate = applicationDate;
        this.userDate = userDate;
        this.status = status;
        this.user = user;
        this.programDate = programDate;
    }
}
