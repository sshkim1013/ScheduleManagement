package com.example.schedulemanagement.repository;

import com.example.schedulemanagement.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {
    Schedule saveSchedule(Schedule schedule);
    List<Schedule> findAllSchedules();
    Schedule findScheduleById(Long id);
    void deleteSchedule(Long id);
}
