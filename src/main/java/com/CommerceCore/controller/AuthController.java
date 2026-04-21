package com.CommerceCore.controller;

import com.CommerceCore.dto.LoginRequestDto;
import com.CommerceCore.dto.UserRequestDto;
import com.CommerceCore.dto.UserResponseDto;
import com.CommerceCore.entity.AuthResponse;
import com.CommerceCore.exception.ApiException;
import com.CommerceCore.service.AuthService;
import com.CommerceCore.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletResponse response){
        AuthResponse auth=service.login(requestDto.getEmail(), requestDto.getPassword());
        setRefreshCookie(response, auth.getRefreshToken());
        return ResponseEntity.ok(
                AuthResponse.builder()
                        .accessToken(auth.getAccessToken())
                        .refreshToken(auth.getRefreshToken())
                        .build()
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request, HttpServletResponse response){
        String oldToken=extractRefreshToken(request);
        AuthResponse auth=service.refresh(oldToken);
        setRefreshCookie(response,auth.getRefreshToken());
        return ResponseEntity.ok(
                AuthResponse.builder()
                        .accessToken(auth.getAccessToken())
                        .refreshToken(auth.getRefreshToken())
                        .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
        String header=request.getHeader("Authorization");
        if(header==null || !header.startsWith("Bearer ")){
            throw new ApiException("Access Token Missing",HttpStatus.FORBIDDEN);
        }
        String accessToken=header.substring(7);
        String refreshToken=extractRefreshToken(request);
        service.logout(accessToken,refreshToken);
        clearCookie(response);
        return ResponseEntity.ok("Logged Out Successfully");
    }

    // Helpers
    private String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new ApiException("Refresh token missing", HttpStatus.UNAUTHORIZED);
        }
        String latestToken = null;
        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                latestToken = cookie.getValue(); // keep overwriting
            }
        }
        if (latestToken == null) {
            throw new ApiException("Refresh token not found", HttpStatus.UNAUTHORIZED);
        }
        return latestToken;
    }

    private void setRefreshCookie(HttpServletResponse response,String token){
        Cookie cookie=new Cookie("refreshToken",token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7*24*60*60);
        response.addCookie(cookie);
    }

    private void clearCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
