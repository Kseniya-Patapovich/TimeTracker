package com.timetracker.utils;

import com.timetracker.model.enums.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    public Role getUserRole(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Role.valueOf(user.getAuthorities().stream().findFirst().get().getAuthority());
    }
}
