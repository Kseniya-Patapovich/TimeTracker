package com.timetracker.security.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class RegistrationDto {
    private String login;
    private String password;
    private String username;
}
