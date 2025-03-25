package com.example.nurim.domain.notice.entity;

import com.example.nurim.domain.common.entity.Timestamped;
import com.example.nurim.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "notices")
public class Notice extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Notice(String title, String contents, User user) {
        this.title = title;
        this.contents = contents;
        this.user = user;
    }

    public void updateNotice(String title,String contents){
        if(title != null){
            this.title = title;
        }
        if(contents != null){
            this.contents = contents;
        }
    }
}
