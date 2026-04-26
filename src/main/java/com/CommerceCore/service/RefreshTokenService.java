package com.CommerceCore.service;

import com.CommerceCore.entity.RefreshToken;
import com.CommerceCore.entity.User;
import com.CommerceCore.repository.RefreshTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepo tokenRepo;
    private static final int REFRESH_EXPIRY_DAYS = 7;

    // Create Refresh Token
    public RefreshToken create(User user){
        RefreshToken token=RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusDays(REFRESH_EXPIRY_DAYS))
                .user(user)
                .revoked(false)
                .build();
        return tokenRepo.save(token);
    }

    // Verify Refresh Token
    public RefreshToken verify(String token){
        RefreshToken refreshToken=tokenRepo.findByToken(token).orElseThrow(()->new RuntimeException("Invalid Refresh Token"));
        if(refreshToken.isRevoked()){
            throw new RuntimeException("Token Revoked");
        }
        if(refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token Expired");
        }
        return refreshToken;
    }

    // Revoke Token
    public void revoke(RefreshToken token){
        token.setRevoked(true);
        tokenRepo.save(token);
    }
    @Transactional
    // Delete all tokens for logout
    public void deleteByUser(User user){
        tokenRepo.deleteByUser(user);
    }
}
