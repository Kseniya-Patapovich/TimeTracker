package com.timetracker.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class RecordDto {
    private Long userId;
    private Long projectId;
    private Integer time;
}
