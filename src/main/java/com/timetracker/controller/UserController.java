package com.timetracker.controller;

import com.timetracker.model.TimeTrackerUser;
import com.timetracker.model.dto.UserCreateDto;
import com.timetracker.service.UserService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<TimeTrackerUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public TimeTrackerUser getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/projects/{projectId}")
    public List<TimeTrackerUser> getUsersByProjectId(@PathVariable Long projectId) {
        return userService.getUsersByProjectId(projectId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long createUser(@RequestBody UserCreateDto userCreateDto) {
        return userService.createUser(userCreateDto);
    }

    @PutMapping("/changePassword")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody String password) {
        userService.changePassword(password);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRole(@PathVariable long id, @PathParam("role") String role) {
        userService.updateRole(id, role);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/block/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void blockUser(@PathVariable long id, @PathParam("locked") boolean locked) {
        userService.blockUser(id, locked);
    }
}
