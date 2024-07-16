package com.timetracker.utils;

import com.timetracker.model.TimeTrackerUser;
import com.timetracker.model.enums.Role;
import com.timetracker.repository.UserRepository;
import com.timetracker.security.TimeTrackerUserDetails;
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

    public TimeTrackerUserDetails getCurrentUser() {
        return  (TimeTrackerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public TimeTrackerUser getUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id=" + id + " not found!"));
    }
}
