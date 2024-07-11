package com.timetracker.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserUpdateName {
    private String username;
}
