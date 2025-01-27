package com.example.schedulemanagement.service;

import com.example.schedulemanagement.dto.ScheduleRequestDto;
import com.example.schedulemanagement.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);
    List<ScheduleResponseDto> findAllSchedules(String authorName, String modifiedDate);
    ScheduleResponseDto findScheduleById(Long id);
    ScheduleResponseDto updateSchedule(Long id, String task, String authorName, String password);
    ScheduleResponseDto deleteSchedule(Long id, String password);
}
