package com.timetracker.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecordDto {
    private Long userId;
    private Long projectId;
    private Integer time;
    private LocalDateTime recordDate;
}
