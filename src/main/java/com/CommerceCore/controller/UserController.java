package com.CommerceCore.controller;

import com.CommerceCore.dto.UserDto;
import com.CommerceCore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    public UserDto createUser(@RequestBody UserDto dto){
        return service.createUser(dto);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId){
        return service.getUserById(userId);
    }
}
