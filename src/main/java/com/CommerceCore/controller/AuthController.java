package com.CommerceCore.controller;

import com.CommerceCore.dto.LoginRequestDto;
import com.CommerceCore.dto.UserRequestDto;
import com.CommerceCore.dto.UserResponseDto;
import com.CommerceCore.service.AuthService;
import com.CommerceCore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;
    private final UserService userService;

    @PostMapping("/create")
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto dto){
        return userService.createUser(dto);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequestDto dto){
        return service.login(dto.getEmail(), dto.getPassword());
    }
}
