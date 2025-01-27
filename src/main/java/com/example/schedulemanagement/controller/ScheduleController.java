package com.example.schedulemanagement.controller;

import com.example.schedulemanagement.dto.ScheduleRequestDto;
import com.example.schedulemanagement.dto.ScheduleResponseDto;
import com.example.schedulemanagement.entity.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final Map<Long, Schedule> scheduleList = new HashMap<>();

    //일정 생성 메서드
    @PostMapping
    public ScheduleResponseDto createSchedule(
            @RequestBody ScheduleRequestDto dto
    ) {
        //식별자가 1씩 증가하도록 생성.
        Long scheduleId = scheduleList.isEmpty() ? 1 : Collections.max(scheduleList.keySet()) + 1;

        //요청받은 데이터로 Schedule 객체 생성.
        Schedule schedule = new Schedule(scheduleId, dto.getTask(), dto.getAuthorName(),
                dto.getPassword(), dto.getCreateDate());

        //Inmemory DB에 저장.
        scheduleList.put(scheduleId, schedule);

        return new ScheduleResponseDto(schedule);
    }

    //전체 일정 조회 메서드
    @GetMapping
    public List<ScheduleResponseDto> findSchedules(
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) String modifiedDate
    ) {
        List<ScheduleResponseDto> list = new ArrayList<>();

        LocalDate parsedDate = null;
        if (modifiedDate != null && !modifiedDate.isEmpty()) {
            parsedDate = LocalDate.parse(modifiedDate, DateTimeFormatter.ISO_DATE);
        }

        //두 조건 모두 충족 X
        if (authorName == null && modifiedDate == null) {
            for (Schedule schedule : scheduleList.values()) {
                list.add(new ScheduleResponseDto(schedule));
            }

        //조건 중 한 가지만을 충족 or 두 가지를 모두 충족
        } else {
            for (Schedule schedule : scheduleList.values()) {
                boolean aBool = authorName != null && authorName.equals(schedule.getAuthorName());
                boolean dBool = parsedDate != null && parsedDate.equals(schedule.getModifiedDate().toLocalDate());

                if (aBool || dBool) {
                    list.add(new ScheduleResponseDto(schedule));
                }
            }
        }

        //수정일의 내림차순으로 정렬
        list.sort((s1, s2)
                                                -> s2.getModifiedDate().compareTo(s1.getModifiedDate()));

        return list;
    }


    //일정 단일 조회 메서드(식별자 이용)
    @GetMapping("/{id}")
    public ScheduleResponseDto findScheduleById(@PathVariable Long id) {
        return new ScheduleResponseDto(scheduleList.get(id));

    }


    //일정 수정 메서드
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto dto
    ) {

        Schedule schedule = scheduleList.get(id);

        if (dto.getCreateDate() != null || scheduleList.isEmpty()) {
            return new ResponseEntity<>(new ScheduleResponseDto(schedule), HttpStatus.BAD_REQUEST);
        }

        //비밀번호 검증 로직
        if (dto.getPassword().equals(schedule.getPassword())) {
            schedule.update(dto);
        } else {
            return new ResponseEntity<>(new ScheduleResponseDto(schedule), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ScheduleResponseDto(schedule), HttpStatus.OK);

    }


    //일정 삭제 메서드
    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        scheduleList.remove(id);
    }
}
