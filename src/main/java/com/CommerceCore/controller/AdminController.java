package com.CommerceCore.controller;

import com.CommerceCore.dto.CategoryDto;

import com.CommerceCore.dto.UserResponseDto;

import com.CommerceCore.service.CategoryService;
import com.CommerceCore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@EnableMethodSecurity
public class AdminController {
    private final CategoryService categoryService;
    private final UserService userService;
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-users")
    public List<UserResponseDto> getAllUser(){
        return userService.getAllUsers();
    }
}
