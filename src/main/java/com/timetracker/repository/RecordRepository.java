package com.timetracker.repository;

import com.timetracker.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByUserId(Long id);

    List<Record> findAllByProjectId(Long id);

    Optional<Record> findByUserIdAndProjectIdAndRecordDate(Long userId, Long projectId, LocalDate recordDate);

    Boolean existsByUserId(Long id);
}
