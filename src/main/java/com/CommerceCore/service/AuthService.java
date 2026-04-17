package com.CommerceCore.service;

import com.CommerceCore.entity.User;
import com.CommerceCore.repository.UserRepo;
import com.CommerceCore.security.JwtUtil;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(()->new RuntimeException("User Not Found"));
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Invalid Credentials");
        }
        return jwtUtil.generateToken(user.getEmail());
    }
}
