package com.timetracker.service;

import com.timetracker.model.Project;
import com.timetracker.model.enums.ProjectStatus;
import com.timetracker.model.enums.Role;
import com.timetracker.model.UserTimeTracker;
import com.timetracker.model.dto.UserCreateDto;
import com.timetracker.repository.ProjectRepository;
import com.timetracker.repository.RecordRepository;
import com.timetracker.repository.UserRepository;
import com.timetracker.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecordRepository recordRepository;
    private final ProjectRepository projectRepository;
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
        Optional<UserTimeTracker> userFromDb = userRepository.findById(id);
        if (userFromDb.isPresent()) {
            UserTimeTracker user = userFromDb.get();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id=" + id + " not found!");
        }
    }

    public void deleteUser(Long id) {
        Optional<UserTimeTracker> userCheck = getUserById(id);
        if (userCheck.isEmpty() && recordRepository.existsByUserId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with id=" + id + "doesn't have records!");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateRole(Long id, String role) {
        UserTimeTracker userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id=" + id + " not found!"));
        if (userUtils.getUserRole(id).ordinal() >= userToUpdate.getRole().ordinal()) {
            userToUpdate.setRole(Role.valueOf(role.toUpperCase().trim()));
            userRepository.save(userToUpdate);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin can only change roles for users with role USER!");
        }
    }

    public List<UserTimeTracker> getUsersByProjectId(Long id) {
        Optional<Project> projectFromDb = projectRepository.findById(id);
        if (projectFromDb.isPresent()) {
            Project project = projectFromDb.get();
            if (project.getProjectStatus() == ProjectStatus.DRAFT) {
                return project.getUsers();
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Project with id=" + id + " is not DRAFT status");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project with id=" + id + " not found!");
        }
    }

    @Transactional
    public void blockUser(Long id, boolean locked) {
        UserTimeTracker userToBlock = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id=" + id + " not found!"));
        if (userUtils.getUserRole(id).ordinal() <= userToBlock.getRole().ordinal()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin can only block/unblock users with role USER!");
        }
        userToBlock.setLocked(locked);
        userRepository.save(userToBlock);
    }
}
