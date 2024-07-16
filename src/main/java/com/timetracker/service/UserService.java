package com.timetracker.service;

import com.timetracker.model.enums.Role;
import com.timetracker.model.UserTimeTracker;
import com.timetracker.model.dto.UserCreateDto;
import com.timetracker.repository.RecordRepository;
import com.timetracker.repository.UserRepository;
import com.timetracker.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecordRepository recordRepository;
    private final UserUtils userUtils;

    public List<UserTimeTracker> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserTimeTracker> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public Long createUser(UserCreateDto userCreateDto) {
        UserTimeTracker user = new UserTimeTracker();
        user.setFullName(userCreateDto.getFullName());
        user.setRole(Role.USER);
        user.setLogin(userCreateDto.getLogin());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        UserTimeTracker createdUser = userRepository.save(user);
        return createdUser.getId();
    }

    @Transactional
    public void updatePassword(String password, Long id) {
        UserTimeTracker user = userUtils.getUser(id);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        Optional<UserTimeTracker> userCheck = getUserById(id);
        if (userCheck.isEmpty() || recordRepository.existsByUserId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with id=" + id + "doesn't have records!");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateRole(Long id, String role) {
        UserTimeTracker userToUpdate = userUtils.getUser(id);
        int currentUserRole = userUtils.getUserRole(id).ordinal();
        Role newRole = null;
        for (Role roleValue: Role.values()){
            if (roleValue.name().equals(role.toUpperCase())){
                newRole = Role.valueOf(role.toUpperCase());
                break;
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Role entered incorrectly!");
            }
        }
        if (newRole.ordinal() != Role.SUPER_ADMIN.ordinal() && currentUserRole >= userToUpdate.getRole().ordinal() && currentUserRole >= newRole.ordinal()) {
            userToUpdate.setRole(newRole);
            userRepository.save(userToUpdate);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You can only change the role for users with a smaller role!");
        }
    }

    public List<UserTimeTracker> getUsersByProjectId(Long id) {
        return userRepository.getAllByProjectId(id);
    }

    @Transactional
    public void blockUser(Long id, boolean locked) {
        UserTimeTracker userToBlock = userUtils.getUser(id);
        if (userUtils.getUserRole(id).ordinal() <= userToBlock.getRole().ordinal()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin can only block/unblock users with role USER!");
        }
        userToBlock.setLocked(locked);
        userRepository.save(userToBlock);
    }
}
