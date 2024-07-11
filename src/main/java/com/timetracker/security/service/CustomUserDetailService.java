package com.timetracker.security.service;

import com.timetracker.security.model.UserSecurity;
import com.timetracker.security.repository.UserSecurityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserSecurityRepository userSecurityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserSecurity> userSecurityInfoOptional = userSecurityRepository.findByUserLogin(username);
        if (userSecurityInfoOptional.isEmpty()) {
            throw new UsernameNotFoundException("Username not found: " + username);
        }
        UserSecurity userSecurity = userSecurityInfoOptional.get();
        return User.builder()
                .username(userSecurity.getUserLogin())
                .password(userSecurity.getUserPassword())
                .roles(userSecurity.getRole().toString())
                .build();
    }
}
