package com.timetracker.security;

import com.timetracker.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**"))
                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")
                );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/users").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/users").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/projects").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/projects/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/projects").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/projects/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/projects/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/records").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/records/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/records").hasAuthority(Role.USER.name())
                        .requestMatchers(HttpMethod.PUT, "/records").hasAuthority(Role.USER.name())
                        .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
