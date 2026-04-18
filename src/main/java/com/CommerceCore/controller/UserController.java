package com.CommerceCore.controller;

import com.CommerceCore.dto.UserResponseDto;
import com.CommerceCore.entity.CustomUserPrincipal;
import com.CommerceCore.repository.UserRepo;
import com.CommerceCore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final UserRepo rep;
    @GetMapping
    public UserResponseDto getUserById(Authentication auth){
        CustomUserPrincipal user=(CustomUserPrincipal) auth.getPrincipal();
        Long userId= user.getId();
        return service.getUserById(userId);
    }
}
