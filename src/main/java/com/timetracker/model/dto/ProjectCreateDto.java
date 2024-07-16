package com.timetracker.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectCreateDto {
    private String projectName;
    private List<Long> usersId;
}
