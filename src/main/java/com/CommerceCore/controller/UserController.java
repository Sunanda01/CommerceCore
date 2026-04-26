package com.CommerceCore.controller;

import com.CommerceCore.dto.UserResponseDto;
import com.CommerceCore.security.CustomUserPrincipal;
import com.CommerceCore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    @GetMapping
    public UserResponseDto getUserById(Authentication auth){
        CustomUserPrincipal user=(CustomUserPrincipal) auth.getPrincipal();
        Long userId= user.getId();
        return service.getUserById(userId);
    }
}
