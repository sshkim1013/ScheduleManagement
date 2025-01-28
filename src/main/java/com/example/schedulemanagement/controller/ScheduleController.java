package com.example.schedulemanagement.controller;

import com.example.schedulemanagement.dto.ScheduleRequestDto;
import com.example.schedulemanagement.dto.ScheduleResponseDto;
import com.example.schedulemanagement.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    //일정 생성 메서드
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @RequestBody ScheduleRequestDto dto
    ) {
        return new ResponseEntity<>(scheduleService.saveSchedule(dto), HttpStatus.CREATED);
    }

    //전체 일정 조회 메서드
    @GetMapping
    public List<ScheduleResponseDto> findAllSchedules(
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) String modifiedDate,
            @RequestParam Map<String, String> queryParams
    ) {
        return scheduleService.findAllSchedules(authorName, modifiedDate, queryParams);
    }


    //일정 단일 조회 메서드(식별자 이용)
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id) {
        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }


    //일정 수정 메서드
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto dto
    ) {
        return new ResponseEntity<>(scheduleService.updateSchedule(id, dto.getTask(), dto.getAuthorName(), dto.getPassword()), HttpStatus.OK);
    }


    //일정 삭제 메서드
    @DeleteMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> deleteSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto dto
    ) {
        scheduleService.deleteSchedule(id, dto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
