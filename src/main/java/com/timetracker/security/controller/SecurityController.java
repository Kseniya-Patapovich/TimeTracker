package com.timetracker.security.controller;

import com.timetracker.security.model.AuthRequestDto;
import com.timetracker.security.model.AuthResponseDto;
import com.timetracker.security.model.RegistrationDto;
import com.timetracker.security.service.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
public class SecurityController {
    private final SecurityService securityService;

    @PostMapping("/token")
    public ResponseEntity<AuthResponseDto> generateToken(@RequestBody AuthRequestDto authRequestDto) {
        Optional<String> token = securityService.generateToken(authRequestDto);
        return token.map(s -> new ResponseEntity<>(new AuthResponseDto(s), HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody RegistrationDto registrationDto) {
        securityService.registration(registrationDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
