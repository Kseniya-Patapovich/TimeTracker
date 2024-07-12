package com.timetracker.service;

import com.timetracker.model.Roles;
import com.timetracker.model.Users;
import com.timetracker.model.dto.UserCreateDto;
import com.timetracker.model.dto.UserUpdateName;
import com.timetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Boolean createUser(UserCreateDto userCreateDto) {
        Users user = new Users();
        user.setUsername(userCreateDto.getUsername());
        user.setRole(Roles.USER);
        user.setLogin(userCreateDto.getUserLogin());
        user.setPassword(passwordEncoder.encode(userCreateDto.getUserPassword()));
        Users createdUser = userRepository.save(user);
        return getUserById(createdUser.getId()).isPresent();
    }

    public Boolean updateUsername(UserUpdateName userUpdateName, Long userId) {
        Optional<Users> userFromDb = userRepository.findById(userId);
        if (userFromDb.isPresent()) {
            Users user = userFromDb.get();
            user.setUsername(userUpdateName.getUsername());
            Users updatedUser = userRepository.saveAndFlush(user);
            return user.equals(updatedUser);
        }
        return false;
    }

    public Boolean deleteUser(Long id) {
        Optional<Users> userCheck = getUserById(id);
        if (userCheck.isEmpty()) {
            return false;
        }
        userRepository.deleteById(id);
        return getUserById(id).isEmpty();
    }
}
