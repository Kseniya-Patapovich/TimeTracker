package com.timetracker.service;

import com.timetracker.model.UserTimeTracker;
import com.timetracker.repository.UserRepository;
import com.timetracker.security.CustomUserDetailsService;
import com.timetracker.security.JwtUtils;
import com.timetracker.model.dto.AuthRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final AuthenticationManager authManager;

    public String login(AuthRequestDto authRequestDto) {
        //Optional<UserTimeTracker> user = userRepository.findByLogin(authRequestDto.getLogin());
        //User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

       /* final String encode = passwordEncoder.encode(authRequestDto.getPassword());
        log.info("Password {} {}",encode , authRequestDto.getPassword());*/
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authRequestDto.getLogin(), authRequestDto.getPassword());
        Authentication auth = authManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(auth);
        return jwtUtils.generateJwtToken(authRequestDto.getLogin());


    }
}
