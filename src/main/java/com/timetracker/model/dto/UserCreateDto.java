package com.timetracker.model.dto;

import com.timetracker.model.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserCreateDto {
    @NotNull
    private String fullName;

    @NotNull
    private String login;

    @NotNull
    private String password;

    @NotNull
    private String role;
}
