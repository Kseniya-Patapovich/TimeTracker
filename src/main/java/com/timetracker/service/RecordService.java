package com.timetracker.service;

import com.timetracker.model.Project;
import com.timetracker.model.UserTimeTracker;
import com.timetracker.model.dto.RecordDto;
import com.timetracker.model.enums.ProjectStatus;
import com.timetracker.model.Record;
import com.timetracker.repository.ProjectRepository;
import com.timetracker.repository.RecordRepository;
import com.timetracker.repository.UserRepository;
import com.timetracker.utils.ProjectUtils;
import com.timetracker.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final UserUtils userUtils;
    private final ProjectUtils projectUtils;

    public List<Record> getAllRecord() {
        return recordRepository.findAll();
    }

    public Optional<Record> getRecordById(Long id) {
        return recordRepository.findById(id);
    }

    public List<Record> getRecordByUserId(Long id) {
        UserTimeTracker user = userUtils.getUser(id);
        if (user.getLocked() == true) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with id=" + id + " is blocked!");
        }
        if (!recordRepository.existsByUserId(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No records found for user with id=" + id);
        }
        return recordRepository.findAllByUserId(id);
    }

    public List<Record> getRecordsByProjectId(Long id) {
        Optional<Project> projectFromDb = projectRepository.findById(id);
        if (projectFromDb.isPresent()) {
            return recordRepository.findAllByProjectId(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project with id=" + id + " not found!");
        }
    }

    @Transactional
    public Long createRecord(RecordDto recordCreateDto) {
        Record record = new Record();
        record.setRecordDate(recordCreateDto.getRecordDate());
        UserTimeTracker user = userUtils.getUser(recordCreateDto.getUserId());
        if (user.getLocked() == false) {
            record.setUser(user);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with id=" + recordCreateDto.getUserId() + " is blocked!");
        }
        Project project = projectUtils.getProject(recordCreateDto.getProjectId());
        if (project.getProjectStatus() != ProjectStatus.DONE) {
            record.setProject(project);
            project.setProjectStatus(ProjectStatus.IN_PROGRESS);
            projectRepository.save(project);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project with id=" + recordCreateDto.getProjectId() + " is in wrong status!");
        }
        record.setSpent(recordCreateDto.getTime());
        Record createdRecord = recordRepository.save(record);
        return createdRecord.getId();
    }

    @Transactional
    public void logTime(RecordDto recordDto) {
        Project project = projectUtils.getProject(recordDto.getProjectId());
        if (project.getProjectStatus() != ProjectStatus.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project with id=" + recordDto.getProjectId() + " is in wrong status!");
        }

        UserTimeTracker user = userUtils.getUser(recordDto.getUserId());
        if (user.getLocked() == true) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with id=" + recordDto.getUserId() + " is blocked!");
        }
        Record record = recordRepository.findByUserIdAndProjectIdAndRecordDate(recordDto.getUserId(), recordDto.getProjectId(), recordDto.getRecordDate());
               // .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found record with userId=" + recordDto.getUserId() + " and projectId=" + recordDto.getProjectId()));
        int totalSpentTime = record.getSpent() + recordDto.getTime();
        if (totalSpentTime > 12 * 60) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Today is the time for logs completed!");
        }
        record.setRecordDate(recordDto.getRecordDate());
        record.setSpent(totalSpentTime);
        recordRepository.save(record);
    }
}
