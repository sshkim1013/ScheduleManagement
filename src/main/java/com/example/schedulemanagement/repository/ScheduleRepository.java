package com.example.schedulemanagement.repository;

import com.example.schedulemanagement.dto.ScheduleResponseDto;
import com.example.schedulemanagement.entity.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    ScheduleResponseDto saveSchedule(Schedule schedule);
    List<ScheduleResponseDto> findAllSchedules();
    Optional<Schedule> findScheduleById(Long id);
    int updateSchedule(Long id, String task, String authorName);
    int deleteSchedule(Long id);
}
