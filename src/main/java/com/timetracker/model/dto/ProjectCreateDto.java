package com.timetracker.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ProjectCreateDto {
    private String name;
}
