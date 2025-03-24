package com.example.nurim.domain.application.entity;

import com.example.nurim.domain.application.enums.ApplicationStatus;
import com.example.nurim.domain.common.entity.Timestamped;
import com.example.nurim.domain.program.entity.ProgramDate;
import com.example.nurim.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "applications")
public class Application extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_date_id", nullable = false)
    private ProgramDate programDate;
}
