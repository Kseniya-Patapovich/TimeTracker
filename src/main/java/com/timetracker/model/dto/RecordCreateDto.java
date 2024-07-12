package com.timetracker.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Data
@Component
public class RecordCreateDto {
    private Timestamp startTime;
    private Timestamp endTime;
    private Long userId;
    private Long projectId;
}
