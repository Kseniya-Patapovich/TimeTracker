package com.timetracker.service;

import com.timetracker.model.Project;
import com.timetracker.model.Users;
import com.timetracker.model.dto.RecordDto;
import com.timetracker.model.enums.ProjectStatus;
import com.timetracker.model.Record;
import com.timetracker.repository.ProjectRepository;
import com.timetracker.repository.RecordRepository;
import com.timetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User with id=" + id + " is blocked!");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id=" + id + " not found!");
        }
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
        record.setRecordDate(LocalDateTime.now());
        Optional<Users> usersOptional = userRepository.findById(recordCreateDto.getUserId());
        if (usersOptional.isPresent()) {
            Users user = usersOptional.get();
            if (!user.getLocked()) {
                record.setUser(user);
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User with id=" + recordCreateDto.getUserId() + " is blocked!");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id=" + recordCreateDto.getUserId() + " not found!");
        }
        Optional<Project> projectOptional = projectRepository.findById(recordCreateDto.getProjectId());
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            if (project.getProjectStatus() != ProjectStatus.DONE) {
                record.setProject(project);
                project.setProjectStatus(ProjectStatus.IN_PROGRESS);
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Project with id=" + recordCreateDto.getProjectId() + " is in wrong status!");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project with id=" + recordCreateDto.getProjectId() + " not found!");
        }
        record.setSpent(recordCreateDto.getTime());
        Record createdRecord = recordRepository.save(record);
        return createdRecord.getId();
    }

    @Transactional
    public void logTime(RecordDto recordDto) {
        Optional<Project> projectFromDb = projectRepository.findById(recordDto.getProjectId());
        if (projectFromDb.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project with id=" + recordDto.getProjectId() + " not found!");

        }
        Project project = projectFromDb.get();
        if (project.getProjectStatus() != ProjectStatus.IN_PROGRESS) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project with id=" + recordDto.getProjectId() + " is in wrong status!");
        }
        Optional<Users> userFromDb = userRepository.findById(recordDto.getUserId());
        if (userFromDb.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id=" + recordDto.getUserId() + " not found!");
        }
        Users user = userFromDb.get();
        if (user.getLocked()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with id=" + recordDto.getUserId() + " is blocked!");
        }
        Record record = recordRepository.findByUserIdAndProjectId(recordDto.getUserId(), recordDto.getProjectId());
        if (record.getSpent() + recordDto.getTime() > 12 * 60) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Today is the time for logs completed!");
        }
        record.setSpent(record.getSpent() + recordDto.getTime());
        recordRepository.save(record);
    }
}
