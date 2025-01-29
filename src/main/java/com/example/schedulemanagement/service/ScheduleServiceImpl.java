package com.example.schedulemanagement.service;

import com.example.schedulemanagement.dto.ScheduleRequestDto;
import com.example.schedulemanagement.dto.ScheduleResponseDto;
import com.example.schedulemanagement.entity.Schedule;
import com.example.schedulemanagement.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {
        Schedule schedule = new Schedule(dto.getTask(), dto.getAuthorName(), dto.getPassword());
        return scheduleRepository.saveSchedule(schedule);
    }


    @Override
    public List<ScheduleResponseDto> findAllSchedules(String authorName, String modifiedDate, Map<String, String> queryParams) {
        if (!queryParams.keySet().stream().allMatch(param -> param.equals("authorName") || param.equals("modifiedDate"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid parameter included in the request.");
        }

        List<ScheduleResponseDto> scheduleResponseList = new ArrayList<>();
        LocalDate parsedDate = null;

        if (modifiedDate != null && !modifiedDate.isEmpty()) {
            try {
                parsedDate = LocalDate.parse(modifiedDate, DateTimeFormatter.ISO_DATE);
            } catch (DateTimeParseException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format. Please use 'yyyy-MM-dd'.");
            }
        }

        //전체 조회: 파라미터가 없는 경우
        if (authorName == null && modifiedDate == null) {
            for (ScheduleResponseDto schedule : scheduleRepository.findAllSchedules()) {
                scheduleResponseList.add(schedule);
            }

        } else {
            for (ScheduleResponseDto schedule : scheduleRepository.findAllSchedules()) {
                boolean matchesAuthor = (authorName != null) && authorName.equals(schedule.getAuthorName());
                boolean matchesDate = (parsedDate != null) && parsedDate.equals(schedule.getModifiedDate().toLocalDate());

                if ((matchesAuthor && matchesDate) ||
                        (matchesAuthor && modifiedDate == null) ||
                                (matchesDate && authorName == null)) {
                    scheduleResponseList.add(schedule);
                }
            }
        }

        return scheduleResponseList;
    }


    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findScheduleById(id);

        if (optionalSchedule.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        return new ScheduleResponseDto(optionalSchedule.get());
    }

    @Transactional
    @Override
    public ScheduleResponseDto updateSchedule(Long id, String task, String authorName, String password) {
        Optional<Schedule> schedule = scheduleRepository.findScheduleById(id);

        if (schedule.get() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        if (task == null || authorName == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The task and authorName are required values!");
        }

        // 저장된 password와 입력한 password 비교
        if (!password.equals(schedule.get().getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You're typing wrong password!");
        }

        int updatedRow = scheduleRepository.updateSchedule(id, task, authorName);
        if (updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        } else {
            return new ScheduleResponseDto(schedule.get());
        }
    }


    @Override
    public ScheduleResponseDto deleteSchedule(Long id, String password) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findScheduleById(id);

        if (optionalSchedule.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        if (password.equals(optionalSchedule.get().getPassword())) {
            int deletedRow = scheduleRepository.deleteSchedule(id);

            if (deletedRow == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
            }

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You're typing wrong password!");
        }

        return new ScheduleResponseDto(optionalSchedule.get());
    }
}
