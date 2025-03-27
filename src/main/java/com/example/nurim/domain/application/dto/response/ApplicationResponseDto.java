package com.example.nurim.domain.application.dto.response;

import com.example.nurim.domain.application.entity.Application;
import com.example.nurim.domain.application.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApplicationResponseDto {
    private final Long id;
    private final Long programDateId;
    private final String programTitle;
    private final LocalDateTime applicationDate;
    private final LocalDateTime useDate;
    private final ApplicationStatus status;

    public ApplicationResponseDto(Application application) {
        this.id = application.getId();
        this.programDateId = application.getProgramDate().getId();
        this.programTitle = application.getProgramDate().getProgram().getTitle();
        this.applicationDate = application.getCreatedAt();
        this.useDate = application.getProgramDate().getDate();
        this.status = application.getStatus();
    }
}
