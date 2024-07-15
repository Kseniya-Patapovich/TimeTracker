package com.timetracker.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserUpdatePasswordDto {
    @NotNull
    private String password;
}
