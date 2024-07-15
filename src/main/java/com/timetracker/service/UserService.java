package com.timetracker.service;

import com.timetracker.exception.UserNotFoundException;
import com.timetracker.model.enums.Roles;
import com.timetracker.model.Users;
import com.timetracker.model.dto.UserCreateDto;
import com.timetracker.model.dto.UserUpdatePasswordDto;
import com.timetracker.repository.RecordRepository;
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
    private final RecordRepository recordRepository;

    public List<Users> getAllUsersAndAdmins() {
        return userRepository.findAll();
    }

    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Long createUser(UserCreateDto userCreateDto) {
        Users user = new Users();
        user.setFullName(userCreateDto.getFullName());
        user.setRole(Roles.USER);
        user.setLogin(userCreateDto.getLogin());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        Users createdUser = userRepository.save(user);
        return createdUser.getId();
    }

    public Boolean updatePassword(UserUpdatePasswordDto userUpdatePasswordDto, Long id) {
        Optional<Users> userFromDb = userRepository.findById(id);
        if (userFromDb.isPresent()) {
            Users user = userFromDb.get();
            user.setPassword(passwordEncoder.encode(userUpdatePasswordDto.getPassword()));
            Users updateUser = userRepository.saveAndFlush(user);
            return user.equals(updateUser);
        }
        return false;
    }

    public Boolean deleteUser(Long id) {
        Optional<Users> userCheck = getUserById(id);
        if (userCheck.isEmpty() && recordRepository.findAllByUserId(id).isEmpty()) {
            return false;
        }
        userRepository.deleteById(id);
        return getUserById(id).isEmpty();
    }

    public void updateRole(Long id) {
        Optional<Users> userFromDb = userRepository.findById(id);
        if (userFromDb.isPresent()) {
            Users user = userFromDb.get();
            user.setRole(Roles.ADMIN);
            userRepository.saveAndFlush(user);
        } else {
            throw new UserNotFoundException(id.toString());
        }
    }

    public void blockUser(Long id) {
        Optional<Users> userFromDb = userRepository.findById(id);
        if (userFromDb.isPresent()) {
            Users user = userFromDb.get();
            if (user.getLocked() && user.getRole() == Roles.USER) {
                user.setLocked(true);
                userRepository.saveAndFlush(user);
            }
        } else {
            throw new UserNotFoundException(id.toString());
        }
    }

    public void unblockUser(Long id) {
        Optional<Users> userFromDb = userRepository.findById(id);
        if (userFromDb.isPresent()) {
            Users user = userFromDb.get();
            if (!user.getLocked() && user.getRole() == Roles.USER) {
                user.setLocked(false);
                userRepository.saveAndFlush(user);
            }
        }
    }

    public void blockAdmin(Long id) {
        Optional<Users> userFromDb = userRepository.findById(id);
        if (userFromDb.isPresent()) {
            Users user = userFromDb.get();
            if (user.getRole() == Roles.ADMIN && user.getLocked()) {
                user.setLocked(true);
                userRepository.saveAndFlush(user);
            }
        }
    }

    public void unblockAdmin(Long id) {
        Optional<Users> userFromDb = userRepository.findById(id);
        if (userFromDb.isPresent()) {
            Users user = userFromDb.get();
            if (!user.getLocked() && user.getRole() == Roles.ADMIN) {
                user.setLocked(false);
                userRepository.saveAndFlush(user);
            }
        }
    }

    public List<Users> getAllUsers() {
        return userRepository.findByRole(Roles.USER);
    }

    public List<Users> getAllAdmins() {
        return userRepository.findByRole(Roles.ADMIN);
    }
}
