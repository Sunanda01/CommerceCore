package com.CommerceCore.controller;

import com.CommerceCore.dto.LoginRequestDto;
import com.CommerceCore.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto dto){
        return service.login(dto.getEmail(), dto.getPassword());
    }
}
