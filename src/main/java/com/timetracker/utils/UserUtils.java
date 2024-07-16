package com.timetracker.utils;

import com.timetracker.model.UserTimeTracker;
import com.timetracker.model.enums.Role;
import com.timetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class UserUtils {
    private final UserRepository userRepository;

    public Role getUserRole(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Role.valueOf(user.getAuthorities().stream().findFirst().get().getAuthority());
    }

    public UserTimeTracker getUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id=" + id + " not found!"));
    }
}
