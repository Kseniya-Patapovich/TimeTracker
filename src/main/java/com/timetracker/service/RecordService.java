package com.timetracker.service;

import com.timetracker.model.Project;
import com.timetracker.model.Record;
import com.timetracker.model.Users;
import com.timetracker.model.dto.RecordCreateDto;
import com.timetracker.repository.ProjectRepository;
import com.timetracker.repository.RecordRepository;
import com.timetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Boolean createRecord(RecordCreateDto recordCreateDto) {
        Record record = new Record();
        record.setStartTime(recordCreateDto.getStartTime());
        record.setEndTime(recordCreateDto.getEndTime());
        Optional<Users> userFromDb = userRepository.findById(recordCreateDto.getUserId());
        userFromDb.ifPresent(record::setUser);
        Optional<Project> projectFromDb = projectRepository.findById(recordCreateDto.getProjectId());
        projectFromDb.ifPresent(record::setProject);

        Record createdRecord = recordRepository.save(record);
        return getRecordById(createdRecord.getId()).isPresent();
    }


}
