package com.timetracker.controller;

import com.timetracker.model.Users;
import com.timetracker.model.dto.UserCreateDto;
import com.timetracker.model.enums.Roles;
import com.timetracker.service.UserService;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Users getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/projects/{id}")
    public List<Users> getUsersByProjectId(@PathVariable Long id) {
        return userService.getUsersByProjectId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createUser(@RequestBody UserCreateDto userCreateDto) {
        return userService.createUser(userCreateDto);
    }

    @PutMapping("/changePassword/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserPassword(@RequestBody String password,
                                   @PathVariable("id") Long id) {
        userService.updatePassword(password, id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRole(@PathVariable Long id, @RequestBody Roles role) {
        userService.updateRole(id, role);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/block/{id}/{locked}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void blockUser(@PathVariable Long id, @PathVariable boolean locked) {
        userService.blockUser(id, locked);
    }
}
