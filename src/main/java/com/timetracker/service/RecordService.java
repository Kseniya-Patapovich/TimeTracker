package com.timetracker.service;

import com.timetracker.model.Project;
import com.timetracker.model.ProjectStatus;
import com.timetracker.model.Record;
import com.timetracker.repository.ProjectRepository;
import com.timetracker.repository.RecordRepository;
import com.timetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public List<Record> getAllRecord() {
        return recordRepository.findAll();
    }

    public Optional<Record> getRecordById(Long id) {
        return recordRepository.findById(id);
    }

    public List<Record> getRecordByUserId(Long id) {
        return recordRepository.findAllByUserId(id);
    }

    public Boolean startTracking(Long userId, Long projectId) {
        Record record = new Record();
        if (userRepository.findById(userId).isPresent()) {
            record.setUser(userRepository.findById(userId).get());
        }
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()) {
            record.setProject(project.get());
            project.get().setProjectStatus(ProjectStatus.IN_PROCESS);
        }
        record.setStartTime(LocalDateTime.now());
        recordRepository.save(record);
        return getRecordById(record.getId()).isPresent();
    }

    public Boolean stopTracking(Long recordId) {
        Optional<Record> recordOptional = getRecordById(recordId);
        if (recordOptional.isPresent()) {
            Record record = recordOptional.get();
            record.setEndTime(LocalDateTime.now());
            record.setTotalTime((double) Duration.between(record.getStartTime(), record.getEndTime()).toHours());
            /*Project project = record.getProject();
            if (Objects.equals(project.getDeadline(), record.getEndTime())) {
                project.setProjectStatus(ProjectStatus.COMPLETED);
            }*/
            Record endTracking = recordRepository.saveAndFlush(record);
            return record.equals(endTracking);
        }
        return false;
    }

    public Double getTotalTime(Long recordId) {
        Optional<Record> recordOptional = recordRepository.findById(recordId);
        if (recordOptional.isPresent()) {
            return recordOptional.get().getTotalTime();
        }
        return (double) Duration.ZERO.toHours();
    }
}
