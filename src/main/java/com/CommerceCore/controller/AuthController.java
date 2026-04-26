package com.CommerceCore.controller;

import com.CommerceCore.util.CookieUtil;
import com.CommerceCore.dto.LoginRequestDto;
import com.CommerceCore.dto.UserRequestDto;
import com.CommerceCore.dto.UserResponseDto;
import com.CommerceCore.dto.AuthResponse;
import com.CommerceCore.exception.ApiException;
import com.CommerceCore.service.AuthService;
import com.CommerceCore.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final CookieUtil cookieUtil;

    @PostMapping("/create")
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto dto){
        return userService.createUser(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletResponse response){
        AuthResponse auth=service.login(requestDto.getEmail(), requestDto.getPassword());
        cookieUtil.setRefreshCookie(response, auth.getRefreshToken());
        return ResponseEntity.ok(
                AuthResponse.builder()
                        .accessToken(auth.getAccessToken())
                        .refreshToken(auth.getRefreshToken())
                        .build()
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request, HttpServletResponse response){
        String oldToken=cookieUtil.extractRefreshToken(request);
        AuthResponse auth=service.refresh(oldToken);
        cookieUtil.setRefreshCookie(response,auth.getRefreshToken());
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
        String refreshToken=cookieUtil.extractRefreshToken(request);
        service.logout(accessToken,refreshToken);
        cookieUtil.clearCookie(response);
        return ResponseEntity.ok("Logged Out Successfully");
    }

    @PostMapping("/logout-all")
    public ResponseEntity<String> logoutAll(HttpServletRequest request, HttpServletResponse response){
        String header=request.getHeader("Authorization");
        if(header==null || !header.startsWith("Bearer ")){
            throw new ApiException("Access Token Missing",HttpStatus.FORBIDDEN);
        }
        String accessToken=header.substring(7);
        String refreshToken=cookieUtil.extractRefreshToken(request);
        service.logoutAll(accessToken,refreshToken);
        cookieUtil.clearCookie(response);
        return ResponseEntity.ok("Logged Out Successfully From All Devices");
    }
}
