package com.timetracker.security;

import com.timetracker.model.enums.Role;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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

    /*@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**"))
                .requestMatchers(new AntPathRequestMatcher("/swagger-ui.html")
                );
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()));
    }*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/users").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/users/projects/**").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/users").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/users/block/**").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/projects").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/projects/**").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/projects").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/projects/changeStatus/**").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/projects/**/user/**").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/projects/**").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/records/project/**").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/records/**").hasAnyAuthority(Role.SUPER_ADMIN.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
