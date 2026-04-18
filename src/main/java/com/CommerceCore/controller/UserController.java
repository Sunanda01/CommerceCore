package com.CommerceCore.controller;

import com.CommerceCore.dto.UserDto;
import com.CommerceCore.dto.UserRequestDto;
import com.CommerceCore.dto.UserResponseDto;
import com.CommerceCore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/{userId}")
    public UserResponseDto getUserById(@PathVariable Long userId){
        return service.getUserById(userId);
    }
}
