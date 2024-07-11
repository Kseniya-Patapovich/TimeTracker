package com.timetracker.security.service;

import com.timetracker.exception.SameUserInDatabase;
import com.timetracker.model.User;
import com.timetracker.repository.UserRepository;
import com.timetracker.security.model.Roles;
import com.timetracker.security.model.UserSecurity;
import com.timetracker.security.model.dto.AuthRequestDto;
import com.timetracker.security.model.dto.RegistrationDto;
import com.timetracker.security.repository.UserSecurityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSecurityService {
    private final PasswordEncoder passwordEncoder;
    private final UserSecurityRepository userSecurityRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public Optional<String> generateToken(AuthRequestDto authRequestDto) {
        Optional<UserSecurity> security = userSecurityRepository.findByUserLogin(authRequestDto.getLogin());
        if (security.isPresent() && passwordEncoder.matches(authRequestDto.getPassword(), security.get().getUserPassword())) {
            return Optional.of(jwtUtils.generateJwtToken(authRequestDto.getLogin()));
        }
        return Optional.empty();
    }

    public void registration(RegistrationDto registrationDto) {
        Optional<UserSecurity> security = userSecurityRepository.findByUserLogin(registrationDto.getLogin());
        if (security.isPresent()) {
            throw new SameUserInDatabase(registrationDto.getLogin());
        }
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        User savedUser = userRepository.save(user);

        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setUserLogin(registrationDto.getLogin());
        userSecurity.setUserPassword(passwordEncoder.encode(registrationDto.getPassword()));
        userSecurity.setUserId(savedUser.getId());
        userSecurity.setRole(Roles.USER);
        userSecurityRepository.save(userSecurity);
    }
}
