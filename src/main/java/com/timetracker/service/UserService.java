package com.timetracker.service;

import com.timetracker.exception.UserNotFoundException;
import com.timetracker.model.enums.Role;
import com.timetracker.model.TimeTrackerUser;
import com.timetracker.model.dto.UserCreateDto;
import com.timetracker.repository.RecordRepository;
import com.timetracker.repository.UserRepository;
import com.timetracker.security.TimeTrackerUserDetails;
import com.timetracker.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecordRepository recordRepository;
    private final UserUtils userUtils;

    public List<TimeTrackerUser> getAllUsers() {
        return userRepository.findAll();
    }

    public TimeTrackerUser getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<TimeTrackerUser> getUsersByProjectId(long projectId) {
        return userRepository.getAllByProjectId(projectId);
    }

    @Transactional
    public long createUser(UserCreateDto userCreateDto) {
        Role role = Role.getByName(userCreateDto.getRole());
        if(role.equals(Role.SUPER_ADMIN)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Super admin creation is forbid!");
        TimeTrackerUser user = new TimeTrackerUser();
        user.setFullName(userCreateDto.getFullName());
        user.setRole(role);
        user.setLogin(userCreateDto.getLogin());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        userRepository.save(user);
        return user.getId();
    }

    @Transactional
    public void changePassword(String password) {
        TimeTrackerUserDetails userDetails = userUtils.getCurrentUser();
        TimeTrackerUser user = userRepository.findById(userDetails.getId()).orElseThrow();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public void deleteUser(long id) {
        TimeTrackerUser userToDelete = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if(userToDelete.getRole().ordinal() >= userUtils.getCurrentUser().getRole().ordinal()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enough permits!");
        }
        if (recordRepository.existsByUserId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete user, cause already have record");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateRole(long id, String role) {
        Role currentUserRole = userUtils.getCurrentUser().getRole();
        TimeTrackerUser userToUpdate = userUtils.getUser(id);
        if (currentUserRole.ordinal() <= userToUpdate.getRole().ordinal()) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enough permits!");
        Role newRole = Role.getByName(role);
        userToUpdate.setRole(newRole);
        userRepository.save(userToUpdate);
    }

    @Transactional
    public void blockUser(long id, boolean locked) {
        TimeTrackerUser userToBlock = userUtils.getUser(id);
        if (userUtils.getCurrentUser().getRole().ordinal() <= userToBlock.getRole().ordinal()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enough permits!");
        }
        userToBlock.setLocked(locked);
        userRepository.save(userToBlock);
    }
}
