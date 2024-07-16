package com.timetracker.model.enums;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;

public enum ProjectStatus implements GrantedAuthority {
    DRAFT, IN_PROGRESS, DONE;

    public static ProjectStatus getByName(String name) {
        try {
            return ProjectStatus.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Unexpected project status value: " + name);
        }
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
