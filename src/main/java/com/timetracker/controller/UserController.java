package com.timetracker.controller;

import com.timetracker.model.Users;
import com.timetracker.model.dto.UserCreateDto;
import com.timetracker.model.dto.UserUpdatePasswordDto;
import com.timetracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Users> getAllUsersAndAdmins() {
        return userService.getAllUsersAndAdmins();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/admins")
    @ResponseStatus(HttpStatus.OK)
    public List<Users> getAllAdmins() {
        return userService.getAllAdmins();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable("id") Long id) {
        Optional<Users> userFromDB = userService.getUserById(id);
        return userFromDB.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createUser(@RequestBody UserCreateDto userCreateDto) {
        return userService.createUser(userCreateDto);
    }

    @PutMapping("/new_password/{id}")
    public ResponseEntity<HttpStatus> updateUserPassword(@RequestBody UserUpdatePasswordDto userUpdatePassword,
                                                         @PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.updatePassword(userUpdatePassword, id)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRole(@PathVariable Long id) {
        userService.updateRole(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.deleteUser(id)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PutMapping("/block/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void blockUser(@PathVariable Long id) {
        userService.blockUser(id);
    }

    @PutMapping("/unblock/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unblockUser(@PathVariable Long id) {
        userService.unblockUser(id);
    }

    @PutMapping("/blockAdmin/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void blockAdmin(@PathVariable Long id) {
        userService.blockAdmin(id);
    }

    @PutMapping("/unblockAdmin/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unblockAdmin(@PathVariable Long id) {
        userService.unblockAdmin(id);
    }
}
