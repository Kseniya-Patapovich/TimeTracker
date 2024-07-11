package com.timetracker.service;

import com.timetracker.model.User;
import com.timetracker.model.dto.UserCreateDto;
import com.timetracker.model.dto.UserUpdateName;
import com.timetracker.model.dto.UserUpdatePassword;
import com.timetracker.security.model.Roles;
import com.timetracker.security.model.UserSecurity;
import com.timetracker.repository.UserRepository;
import com.timetracker.security.repository.UserSecurityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserSecurityRepository userSecurityRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Boolean createUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        User createdUser = userRepository.save(user);

        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setUserPassword(passwordEncoder.encode(userCreateDto.getUserPassword()));
        userSecurity.setUserLogin(userCreateDto.getUserLogin());
        userSecurity.setRole(Roles.USER);
        userSecurity.setUserId(user.getId());
        userSecurityRepository.save(userSecurity);

        return getUserById(createdUser.getId()).isPresent();
    }

    public Boolean updatePassword(UserUpdatePassword userUpdatePassword, Long userId) {
        Optional<UserSecurity> userSecurityOptional = userSecurityRepository.findById(userId);
        if (userSecurityOptional.isPresent()) {
            UserSecurity userSecurity = userSecurityOptional.get();
            userSecurity.setUserPassword(passwordEncoder.encode(userUpdatePassword.getPassword()));
            UserSecurity updatedUser = userSecurityRepository.saveAndFlush(userSecurity);
            return userSecurity.equals(updatedUser);
        }
        return false;
    }

    public Boolean updateUserName(UserUpdateName userUpdateName, Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        if (userFromDb.isPresent()) {
            User user = userFromDb.get();
            user.setUsername(userUpdateName.getUsername());
            User updatedUser = userRepository.saveAndFlush(user);
            return user.equals(updatedUser);
        }
        return false;
    }

    public Boolean deleteUser(Long id) {
        Optional<User> userCheck = getUserById(id);
        if (userCheck.isEmpty()) {
            return false;
        }
        userRepository.deleteById(id);
        return getUserById(id).isEmpty();
    }
}
