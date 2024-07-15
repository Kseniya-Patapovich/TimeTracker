package com.timetracker.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserCreateDto {
    @NotNull
    private String fullName;

    @NotNull
    private String login;

    @NotNull
    private String password;
}
