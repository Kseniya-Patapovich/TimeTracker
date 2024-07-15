package com.timetracker.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Data
@Component
public class RecordCreateDto {
    private Long userId;
    private Long projectId;
    private Integer time;
}
