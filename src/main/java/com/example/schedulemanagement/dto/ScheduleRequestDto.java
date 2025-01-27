package com.example.schedulemanagement.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleRequestDto {
    private String task;
    private String authorName;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
}
