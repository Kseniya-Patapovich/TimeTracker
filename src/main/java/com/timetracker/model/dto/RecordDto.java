package com.timetracker.model.dto;

import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RecordDto {
    private Long userId;
    private Long projectId;
    private Integer time;
    @PastOrPresent
    private LocalDate recordDate;
}
