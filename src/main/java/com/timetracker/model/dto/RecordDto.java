package com.timetracker.model.dto;

import lombok.Data;

@Data
public class RecordDto {
    private Long userId;
    private Long projectId;
    private Integer time;
}
