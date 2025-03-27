package com.example.nurim.domain.program.entity;

import com.example.nurim.domain.common.entity.Timestamped;
import com.example.nurim.domain.program.enums.ProgramStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "programs")
public class Program extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(nullable = false)
    private Long quota;

    @Column(nullable = false)
    private String detail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgramStatus status; // 접수중, 접수종료

    @Column(name = "registration_start_date", nullable = false)
    private LocalDateTime registrationStartDate; // 접수 시작일

    @Column(name = "registration_end_date", nullable = false)
    private LocalDateTime registrationEndDate; // 접수 마감일


    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(length = 50)
    private String phone;

    public Program(Category category, String title, String location, Long quota, String detail, ProgramStatus status,  LocalDateTime registrationStartDate, LocalDateTime registrationEndDate, String phone) {
        this.category = category;
        this.title = title;
        this.location = location;
        this.quota = quota;
        this.detail = detail;
        this.status = status;
        this.registrationStartDate = registrationStartDate;
        this.registrationEndDate = registrationEndDate;
        this.phone = phone;
        this.deletedAt = null;
    }

    // 프로그램 수정(상태값 제외)
    public void update(Category category, String title, String location, Long quota, String detail,  LocalDateTime registrationStartDate, LocalDateTime registrationEndDate, String phone) {
        this.category = category;
        this.title = title;
        this.location = location;
        this.quota = quota;
        this.detail = detail;
        this.registrationStartDate = registrationStartDate;
        this.registrationEndDate = registrationEndDate;
        this.phone = phone;
    }

    public void updateStatus(ProgramStatus status) {
        this.status = status;
    }

    // 소프트 딜리트
    public void delete(LocalDateTime now) {
        deletedAt = now;
    }

}
