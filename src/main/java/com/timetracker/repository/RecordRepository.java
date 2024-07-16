package com.timetracker.repository;

import com.timetracker.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByUserId(long id);

    List<Record> findAllByProjectId(long id);

    Optional<Record> findByUserIdAndProjectIdAndRecordDate(long userId, long projectId, LocalDate recordDate);

    Optional<Record> findByIdAndUserId(long id, long userId);

    Boolean existsByUserId(long id);
}
