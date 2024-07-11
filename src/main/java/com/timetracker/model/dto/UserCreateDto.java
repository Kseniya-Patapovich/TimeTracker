package com.timetracker.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserCreateDto {
    private String username;
    private String userLogin;
    private String userPassword;
}
