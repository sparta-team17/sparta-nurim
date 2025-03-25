package com.example.nurim.domain.program.entity;

import com.example.nurim.domain.common.entity.Timestamped;
import com.example.nurim.domain.program.enums.ProgramStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "usage_start_date", nullable = false)
    private LocalDateTime usageStartDate;

    @Column(name = "usage_end_date", nullable = false)
    private LocalDateTime usageEndDate;


    @Column(name = "registration_start_date", nullable = false)
    private LocalDateTime registrationStartDate;

    @Column(name = "registration_end_date", nullable = false)
    private LocalDateTime registrationEndDate;


    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(length = 50)
    private String phone;

    public Program(Category category, String title, String location, Long quota, String detail, ProgramStatus status, LocalDateTime usageStartDate, LocalDateTime usageEndDate, LocalDateTime registrationStartDate, LocalDateTime registrationEndDate, String phone) {
        this.category = category;
        this.title = title;
        this.location = location;
        this.quota = quota;
        this.detail = detail;
        this.status = status;
        this.usageStartDate = usageStartDate;
        this.usageEndDate = usageEndDate;
        this.registrationStartDate = registrationStartDate;
        this.registrationEndDate = registrationEndDate;
        this.phone = phone;
        this.deletedAt = null;
    }

    // 프로그램 수정(상태값 제외)
    public void update(Category category, String title, String location, Long quota, String detail, LocalDateTime usageStartDate, LocalDateTime usageEndDate, LocalDateTime registrationStartDate, LocalDateTime registrationEndDate, String phone) {
        this.category = category;
        this.title = title;
        this.location = location;
        this.quota = quota;
        this.detail = detail;
        this.usageStartDate = usageStartDate;
        this.usageEndDate = usageEndDate;
        this.registrationStartDate = registrationStartDate;
        this.registrationEndDate = registrationEndDate;
        this.phone = phone;
    }

    // 프로그램 상태값만 수정
    public void updateStatus(ProgramStatus status){
        this.status = status;
    }

    // 소프트 딜리트
    public void delete(){
        this.deletedAt = LocalDateTime.now();
    }

}
