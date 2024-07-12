package com.timetracker.controller;

import com.timetracker.model.Users;
import com.timetracker.model.dto.UserCreateDto;
import com.timetracker.model.dto.UserUpdateLoginDto;
import com.timetracker.model.dto.UserUpdateNameDto;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable("id") Long id) {
        Optional<Users> userFromDB = userService.getUserById(id);
        return userFromDB.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createUser(@RequestBody UserCreateDto userCreateDto) {
        return new ResponseEntity<>(userService.createUser(userCreateDto) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping("/new_username/{id}")
    public ResponseEntity<HttpStatus> updateUsername(@RequestBody UserUpdateNameDto userUpdateName,
                                                     @PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.updateUsername(userUpdateName, id)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PutMapping("/new_password/{id}")
    public ResponseEntity<HttpStatus> updateUserPassword(@RequestBody UserUpdatePasswordDto userUpdatePassword,
                                                         @PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.updatePassword(userUpdatePassword, id)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PutMapping("/new_login/{id}")
    public ResponseEntity<HttpStatus> updateLogin(@RequestBody UserUpdateLoginDto userUpdateLoginDto,
                                                  @PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.updateLogin(userUpdateLoginDto, id)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.deleteUser(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
