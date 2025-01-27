package com.example.schedulemanagement.service;

import com.example.schedulemanagement.dto.ScheduleRequestDto;
import com.example.schedulemanagement.dto.ScheduleResponseDto;
import com.example.schedulemanagement.entity.Schedule;
import com.example.schedulemanagement.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {
        Schedule schedule = new Schedule(dto.getTask(), dto.getAuthorName(), dto.getPassword());
        Schedule savedSchedule = scheduleRepository.saveSchedule(schedule);
        return new ScheduleResponseDto(savedSchedule);
    }


    @Override
    public List<ScheduleResponseDto> findAllSchedules(String authorName, String modifiedDate) {
        List<ScheduleResponseDto> scheduleResponseList = new ArrayList<>();
        LocalDate parsedDate = null;

        if (modifiedDate != null && !modifiedDate.isEmpty()) {
            parsedDate = LocalDate.parse(modifiedDate, DateTimeFormatter.ISO_DATE);
        }

        if (authorName == null && modifiedDate == null) {
            for (Schedule schedule : scheduleRepository.findAllSchedules()) {
                scheduleResponseList.add(new ScheduleResponseDto(schedule));
            }
        } else {
            for (Schedule schedule : scheduleRepository.findAllSchedules()) {
                boolean aBool = authorName != null && authorName.equals(schedule.getAuthorName());
                boolean dBool = parsedDate != null && parsedDate.equals(schedule.getModifiedDate().toLocalDate());

                if (aBool || dBool) {
                    scheduleResponseList.add(new ScheduleResponseDto(schedule));
                }
            }
        }

        //수정일의 내림차순으로 정렬
        scheduleResponseList.sort((s1, s2)
                                        -> s2.getModifiedDate().compareTo(s1.getModifiedDate()));

        return scheduleResponseList;
    }


    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        if (scheduleRepository.findScheduleById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        return new ScheduleResponseDto(scheduleRepository.findScheduleById(id));
    }


    @Override
    public ScheduleResponseDto updateSchedule(Long id, String task, String authorName, String password) {
        Schedule schedule = scheduleRepository.findScheduleById(id);

        if (schedule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        if (task == null || authorName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The task and authorName are required values!");
        }

        if (password.equals(schedule.getPassword())) {
            schedule.update(task, authorName, password);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You're typing wrong password!");
        }

        return new ScheduleResponseDto(schedule);
    }


    @Override
    public ScheduleResponseDto deleteSchedule(Long id, String password) {
        Schedule schedule = scheduleRepository.findScheduleById(id);

        if (schedule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        if (password.equals(schedule.getPassword())) {
            scheduleRepository.deleteSchedule(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You're typing wrong password!");
        }

        return new ScheduleResponseDto(schedule);
    }
}
