package com.timetracker.service;

import com.timetracker.model.Project;
import com.timetracker.model.dto.RecordDto;
import com.timetracker.model.enums.ProjectStatus;
import com.timetracker.model.Record;
import com.timetracker.repository.ProjectRepository;
import com.timetracker.repository.RecordRepository;
import com.timetracker.repository.UserRepository;
import com.timetracker.security.TimeTrackerUserDetails;
import com.timetracker.utils.ProjectUtils;
import com.timetracker.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;
    private final ProjectRepository projectRepository;
    private final UserUtils userUtils;
    private final ProjectUtils projectUtils;
    private final UserRepository userRepository;

    private final static int maxSpentTime = 720; // 12 hours

    public List<Record> getAllRecord() {
        return recordRepository.findAll();
    }

    public Record getRecordById(Long id) {
        return recordRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Record with id=" + id + " not found!"));
    }

    public List<Record> getRecordByUserId(Long id) {
        if (!recordRepository.existsByUserId(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No records found for user with id=" + id);
        }
        return recordRepository.findAllByUserId(id);
    }

    public List<Record> getRecordsByProjectId(Long id) {
        if (projectRepository.existsById(id)) {
            return recordRepository.findAllByProjectId(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project with id=" + id + " not found!");
        }
    }

    @Transactional
    public Long createRecord(RecordDto recordCreateDto) {
        if (recordCreateDto.getRecordDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Date must be in past!");
        }
        TimeTrackerUserDetails userDetails = userUtils.getCurrentUser();
        Project project = projectUtils.getProject(recordCreateDto.getProjectId());
        if (project.getProjectStatus() == ProjectStatus.IN_PROGRESS) {
            Record record = new Record();
            record.setRecordDate(recordCreateDto.getRecordDate());
            record.setProject(project);
            record.setUser(userRepository.findById(userDetails.getId()).orElseThrow());
            record.setSpent(recordCreateDto.getSpentTime());
            recordRepository.save(record);
            return record.getId();
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project with id=" + recordCreateDto.getProjectId() + " is in wrong status!");
        }
    }

    @Transactional
    public void logTime(RecordDto recordDto) {
        if (recordDto.getRecordDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Incorrect date!");
        }
        TimeTrackerUserDetails userDetails = userUtils.getCurrentUser();
        Record record = recordRepository.findByUserIdAndProjectIdAndRecordDate(userDetails.getId(), recordDto.getProjectId(), recordDto.getRecordDate())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found record with userId=" + userDetails.getId() + " and projectId=" + recordDto.getProjectId()));
        int totalSpentTime = record.getSpent() + recordDto.getSpentTime();
        if (totalSpentTime > maxSpentTime) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Today is the time for logs completed!");
        }
        record.setRecordDate(recordDto.getRecordDate());
        record.setSpent(totalSpentTime);
        recordRepository.save(record);
    }

    @Transactional
    public void deleteRecord(long id) {
        TimeTrackerUserDetails userDetails = userUtils.getCurrentUser();
        Record record = recordRepository.findByIdAndUserId(id, userDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found record with id=" + id));
        recordRepository.delete(record);
    }
}
