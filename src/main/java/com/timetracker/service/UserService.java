package com.timetracker.service;

import com.timetracker.model.Project;
import com.timetracker.model.enums.ProjectStatus;
import com.timetracker.model.enums.Roles;
import com.timetracker.model.Users;
import com.timetracker.model.dto.UserCreateDto;
import com.timetracker.repository.ProjectRepository;
import com.timetracker.repository.RecordRepository;
import com.timetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecordRepository recordRepository;
    private final ProjectRepository projectRepository;

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public Long createUser(UserCreateDto userCreateDto) {
        Users user = new Users();
        user.setFullName(userCreateDto.getFullName());
        user.setRole(Roles.USER);
        user.setLogin(userCreateDto.getLogin());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        Users createdUser = userRepository.save(user);
        return createdUser.getId();
    }

    @Transactional
    public void updatePassword(String password, Long id) {
        Optional<Users> userFromDb = userRepository.findById(id);
        if (userFromDb.isPresent()) {
            Users user = userFromDb.get();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id=" + id + " not found!");
        }
    }

    public void deleteUser(Long id) {
        Optional<Users> userCheck = getUserById(id);
        if (userCheck.isEmpty() && recordRepository.existsByUserId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with id=" + id + "doesn't have records!");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateRole(Long id, Roles role) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = principal instanceof UserDetails ? ((UserDetails) principal).getUsername() : principal.toString();
        Users currentUser = userRepository.findByFullName(currentUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found!"));
        Users userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id=" + id + " not found!"));
        if (currentUser.getRole() == Roles.ADMIN && userToUpdate.getRole() != Roles.USER) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin can only change roles for users with role USER!");
        }
        if (currentUser.getRole() == Roles.USER) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User cannot change roles!");
        }
        userToUpdate.setRole(role);
        userRepository.save(userToUpdate);
    }

    public List<Users> getUsersByProjectId(Long id) {
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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = principal instanceof UserDetails ? ((UserDetails) principal).getUsername() : principal.toString();
        Users currentUser = userRepository.findByFullName(currentUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found!"));
        Users userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id=" + id + " not found!"));
        Users userToBlock = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id=" + id + " not found!"));
        if (currentUser.getRole() == Roles.ADMIN && userToUpdate.getRole() != Roles.USER) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin can only block/unblock users with role USER!");
        }
        if (currentUser.getRole() == Roles.USER) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User cannot block/unblock any users");
        }
        userToBlock.setLocked(locked);
        userRepository.save(userToBlock);
    }
}
