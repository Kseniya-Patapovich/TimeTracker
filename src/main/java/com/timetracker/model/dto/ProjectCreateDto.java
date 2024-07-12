package com.timetracker.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Component
public class ProjectCreateDto {
    private String name;
    private LocalDateTime deadline;
}
