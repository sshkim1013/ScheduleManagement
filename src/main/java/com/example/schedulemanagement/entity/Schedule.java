package com.example.schedulemanagement.entity;

/*
- 일정 생성(일정 작성하기)
- 일정 생성 시, 포함되어야할 데이터
- 할일, 작성자명, 비밀번호, 작성/수정일 저장
- 작성/수정일은 날짜와 시간을 모두 포함한 형태
- 각 일정의 고유 식별자(ID)를 자동으로 생성하여 관리
- 최초 입력 시, 수정일은 작성일과 동일
*/

import com.example.schedulemanagement.dto.ScheduleRequestDto;
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
    public Schedule(Long id, String task, String authorName, String password, LocalDateTime createDate) {
        this.id = id;
        this.task = task;
        this.authorName = authorName;
        this.password = password;
        this.createDate = createDate;
        this.modifiedDate = createDate;
    }

    /*
    1. 선택한 일정 내용 중, "할 일", "작성자 명"만 수정 가능.
    2. 서버에 일정 수정을 요청할 때, 비밀번호를 함께 전달.
    3. [클라이언트 -> 서버] 전달 데이터: task, authorName, password.
    4. 작성일 -> 변경 불가.
    5. 수정일 -> 수정 완료 시, 수정한 시점으로 변경.
    */
    public void update(ScheduleRequestDto dto) {
        this.task = dto.getTask();
        this.authorName = dto.getAuthorName();
        this.password = dto.getPassword();
        this.modifiedDate = LocalDateTime.now();
    }
}
