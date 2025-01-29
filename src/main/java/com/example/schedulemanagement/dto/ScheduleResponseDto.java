package com.example.schedulemanagement.dto;

import com.example.schedulemanagement.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private String task;
    private String authorName;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;


    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.task = schedule.getTask();
        this.authorName = schedule.getAuthorName();
        this.password = schedule.getPassword();
        this.createDate = schedule.getCreateDate();
        this.modifiedDate = schedule.getModifiedDate();
    }
}
