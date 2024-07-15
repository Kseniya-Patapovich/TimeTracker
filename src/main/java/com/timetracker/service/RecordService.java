package com.timetracker.service;

import com.timetracker.exception.BlockedUserException;
import com.timetracker.exception.LogTimeException;
import com.timetracker.exception.ProjectIsDoneException;
import com.timetracker.exception.ProjectIsNotInProgressException;
import com.timetracker.exception.ProjectNotFoundException;
import com.timetracker.exception.UserNotFoundException;
import com.timetracker.model.Project;
import com.timetracker.model.Users;
import com.timetracker.model.dto.RecordCreateDto;
import com.timetracker.model.dto.RecordUpdateDto;
import com.timetracker.model.enums.ProjectStatus;
import com.timetracker.model.Record;
import com.timetracker.repository.ProjectRepository;
import com.timetracker.repository.RecordRepository;
import com.timetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
        Optional<Users> userFromDb = userRepository.findById(id);
        if (userFromDb.isPresent()) {
            Users user = userFromDb.get();
            if (user.getLocked()) {
                return recordRepository.findAllByUserId(id);
            } else {
                throw new BlockedUserException(user.getId().toString());
            }
        } else {
            throw new UserNotFoundException(id.toString());
        }
    }

    public List<Record> getRecordsByProjectId(Long id) {
        Optional<Project> projectFromDb = projectRepository.findById(id);
        if (projectFromDb.isPresent()) {
            return recordRepository.findAllByProjectId(id);
        } else {
            throw new ProjectIsDoneException(id.toString());
        }
    }

    public Long createRecord(RecordCreateDto recordCreateDto) {
        Record record = new Record();
        record.setRecordDate(LocalDateTime.now());
        Optional<Users> usersOptional = userRepository.findById(recordCreateDto.getUserId());
        if (usersOptional.isPresent()) {
            Users user = usersOptional.get();
            if (!user.getLocked()) {
                record.setUser(user);
            } else {
                throw new BlockedUserException(user.getId().toString());
            }
        } else {
            throw new UserNotFoundException(recordCreateDto.getUserId().toString());
        }
        Optional<Project> projectOptional = projectRepository.findById(recordCreateDto.getProjectId());
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            if (project.getProjectStatus() != ProjectStatus.DONE) {
                record.setProject(project);
                project.setProjectStatus(ProjectStatus.IN_PROGRESS);
            } else {
                throw new ProjectIsDoneException(project.getProjectStatus().name());
            }
        } else {
            throw new ProjectNotFoundException(recordCreateDto.getProjectId().toString());
        }
        record.setSpent(recordCreateDto.getTime());
        Record createdRecord = recordRepository.save(record);
        return createdRecord.getId();
    }

    public void logTime(Long userId, Long projectId, RecordUpdateDto recordUpdateDto) {
        Optional<Project> projectFromDb = projectRepository.findById(projectId);
        if (projectFromDb.isEmpty()) {
            throw new ProjectNotFoundException(projectId.toString());
        }
        Project project = projectFromDb.get();
        if (project.getProjectStatus() != ProjectStatus.IN_PROGRESS) {
            throw new ProjectIsNotInProgressException(projectId.toString());
        }
        Optional<Users> userFromDb = userRepository.findById(userId);
        if (userFromDb.isEmpty()) {
            throw new UserNotFoundException(userId.toString());
        }
        Users user = userFromDb.get();
        if (user.getLocked()) {
            throw new BlockedUserException(userId.toString());
        }
        Record record = recordRepository.findByUserIdAndProjectId(userId, projectId);
        if (record.getSpent() + recordUpdateDto.getTime() > 12 * 60) {
            throw new LogTimeException(record.getSpent().toString());
        }
        record.setSpent(record.getSpent() + recordUpdateDto.getTime());
        recordRepository.saveAndFlush(record);
    }
}
