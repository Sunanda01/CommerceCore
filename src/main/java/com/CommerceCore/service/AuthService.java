package com.CommerceCore.service;

import com.CommerceCore.entity.User;
import com.CommerceCore.exception.ApiException;
import com.CommerceCore.repository.UserRepo;
import com.CommerceCore.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(String email, String password){
        User user=repo.findByEmail(email)
                .orElseThrow(()->new ApiException("User Not Found", HttpStatus.NOT_FOUND));
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new ApiException("Invalid Credentials",HttpStatus.FORBIDDEN);
        }
        return jwtUtil.generateToken(user.getId(),user.getRole().name());
    }
}
