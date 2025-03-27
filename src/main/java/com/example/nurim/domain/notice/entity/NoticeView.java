package com.example.nurim.domain.notice.entity;

import com.example.nurim.domain.common.entity.Timestamped;
import com.example.nurim.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notice_views")
@NoArgsConstructor
public class NoticeView extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long noticeId;
    @Column(nullable = false)
    private Long userId;

    public NoticeView(Long noticeId, Long userId){
        this.noticeId = noticeId;
        this.userId = userId;
    }

}
