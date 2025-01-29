package com.example.schedulemanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {

    private Long id;
    private String task;
    private String authorName;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;

    //최초 입력 시, 수정일은 작성일과 동일.
    public Schedule(Long id, String task, String authorName, String password) {
        this.id = id;
        this.task = task;
        this.authorName = authorName;
        this.password = password;
        this.createDate = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
    }

    public Schedule(String task, String authorName, String password) {
        this.task = task;
        this.authorName = authorName;
        this.password = password;
        this.createDate = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
    }


    public Schedule update(String task, String authorName, String password) {
        this.task = task;
        this.authorName = authorName;
        this.password = password;
        this.modifiedDate = LocalDateTime.now();

        return this;
    }
}
