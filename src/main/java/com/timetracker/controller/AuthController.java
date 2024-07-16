package com.timetracker.controller;

import com.timetracker.model.dto.AuthRequestDto;
import com.timetracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService securityService;

    @PostMapping
    public String generateToken(@RequestBody AuthRequestDto authRequestDto) {
        return securityService.login(authRequestDto);
    }
}
