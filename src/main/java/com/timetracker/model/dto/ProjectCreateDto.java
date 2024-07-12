package com.timetracker.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
public class ProjectCreateDto {
    private String name;

    @Future
    @NotNull
    private LocalDateTime deadline;
}
