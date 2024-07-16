package com.timetracker.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RecordDto {
    @NotNull
    private long projectId;
    @NotNull
    @Min(1)
    @Max(720)
    private int spentTime;
    @NotNull
    @PastOrPresent
    private LocalDate recordDate;
}
