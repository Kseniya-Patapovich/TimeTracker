package com.timetracker.security;

import com.timetracker.exception.SameUserInDatabase;
import com.timetracker.model.enums.Roles;
import com.timetracker.model.Users;
import com.timetracker.repository.UserRepository;
import com.timetracker.security.dto.AuthRequestDto;
import com.timetracker.security.dto.RegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public Optional<String> generateToken(AuthRequestDto authRequestDto) {
        Optional<Users> user = userRepository.findByLogin(authRequestDto.getLogin());
        if (user.isPresent() && passwordEncoder.matches(authRequestDto.getPassword(), user.get().getPassword())) {
            return Optional.of(jwtUtils.generateJwtToken(authRequestDto.getLogin()));
        }
        return Optional.empty();
    }

    public void registration(RegistrationDto registrationDto) {
        Optional<Users> security = userRepository.findByLogin(registrationDto.getLogin());
        if (security.isPresent()) {
            throw new SameUserInDatabase(registrationDto.getLogin());
        }
        Users user = new Users();
        user.setFullName(registrationDto.getUsername());
        user.setLogin(registrationDto.getLogin());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(Roles.ADMIN);
        Users savedUser = userRepository.save(user);
    }
}
