package com.timetracker.security;

import com.timetracker.model.UserTimeTracker;
import com.timetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserTimeTracker> userInfoOptional = userRepository.findByLogin(username);
        if (userInfoOptional.isEmpty()) {
            throw new UsernameNotFoundException("Username not found: " + username);
        }
        UserTimeTracker user = userInfoOptional.get();
        return User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();
    }
}
