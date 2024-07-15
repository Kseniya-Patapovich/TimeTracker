package com.timetracker.controller;

import com.timetracker.security.dto.AuthRequestDto;
import com.timetracker.security.dto.AuthResponseDto;
import com.timetracker.security.dto.RegistrationDto;
import com.timetracker.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SecurityController {
    private final SecurityService securityService;

    @PostMapping("/token")
    public ResponseEntity<AuthResponseDto> generateToken(@RequestBody AuthRequestDto authRequestDto) {
        Optional<String> token = securityService.generateToken(authRequestDto);
        return token.map(s -> new ResponseEntity<>(new AuthResponseDto(s), HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/admin")
    public ResponseEntity<HttpStatus> registration(@RequestBody RegistrationDto registrationDto) {
        securityService.registration(registrationDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
