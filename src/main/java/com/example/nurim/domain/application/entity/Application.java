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
    @JoinColumn(name = "program_date_id", nullable = false)
    private ProgramDate programDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "use_date", nullable = false)
    private LocalDateTime useDate; // 이용일

    @Column(name = "is_reviewd", nullable = false)
    private boolean isReviewd; // 리뷰 작성 여부

    public Application(ProgramDate programDate, User user, ApplicationStatus status, LocalDateTime userDate) {
        this.programDate = programDate;
        this.user = user;
        this.status = status;
        this.useDate = userDate;
        this.isReviewd = false; // 기본값 : 리뷰 미작성
    }

    public void markAsReviewed() {
        this.isReviewd = true; // 리뷰 작성 완료 처리 메서드 추가
    }

    public void cancelApplication() {
        this.status = ApplicationStatus.CANCEL;
    }
}
