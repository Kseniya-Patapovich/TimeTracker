package com.timetracker.security;

import com.timetracker.model.enums.Roles;
import com.timetracker.security.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/admin").hasRole(Roles.SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/users").hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/users/all").hasRole(Roles.SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/users/admins").hasRole(Roles.SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/users").hasRole(Roles.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/users/new_password/**").hasRole(Roles.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasRole(Roles.SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/users/block/**").hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/users/unblocked/**").hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/users/blockAdmin/**").hasRole(Roles.SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/users/unblockAdmin/**").hasRole(Roles.SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasAnyRole(Roles.ADMIN.name(), Roles.SUPER_ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/projects").hasRole(Roles.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/projects/**").hasRole(Roles.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/projects/by_user/**").hasAnyRole(Roles.ADMIN.name(), Roles.USER.name())
                        .requestMatchers(HttpMethod.GET, "/projects/users/**").hasRole(Roles.ADMIN.name())
                        .requestMatchers(HttpMethod.POST,"/projects").hasRole(Roles.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/projects").hasRole(Roles.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/projects/**").hasRole(Roles.USER.name())
                        .requestMatchers(HttpMethod.DELETE, "/projects/**").hasRole(Roles.ADMIN.name())
                        .requestMatchers(HttpMethod.GET,"/records").hasRole(Roles.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/records/**").hasRole(Roles.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/records/project").hasRole(Roles.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/records/user").hasRole(Roles.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/records").hasRole(Roles.USER.name())
                        .requestMatchers(HttpMethod.PUT, "/records").hasRole(Roles.USER.name())
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
