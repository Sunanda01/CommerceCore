package com.CommerceCore.service;

import com.CommerceCore.entity.AuthProvider;
import com.CommerceCore.dto.AuthResponse;
import com.CommerceCore.entity.RefreshToken;
import com.CommerceCore.entity.User;
import com.CommerceCore.exception.ApiException;
import com.CommerceCore.repository.RefreshTokenRepo;
import com.CommerceCore.repository.UserRepo;
import com.CommerceCore.security.jwt.JwtUtil;
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
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepo tokenRepo;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthResponse login(String email, String password){
        User user=repo.findByEmail(email)
                .orElseThrow(()->new ApiException("User Not Found", HttpStatus.NOT_FOUND));
        if(user.getProvider() == AuthProvider.GOOGLE){
            throw new ApiException("Use Google Login", HttpStatus.BAD_REQUEST);
        }
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new ApiException("Invalid Credentials",HttpStatus.FORBIDDEN);
        }
        String accessToken=jwtUtil.generateToken(user.getId(),user.getRole().name());
        RefreshToken refreshToken=refreshTokenService.create(user);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    // 🔁 REFRESH (ROTATION IMPLEMENTED)
    public AuthResponse refresh(String oldToken) {

        // 1. Validate old token
        RefreshToken existing = refreshTokenService.verify(oldToken);

        // 2. Revoke old token
        refreshTokenService.revoke(existing);

        User user = existing.getUser();

        // 3. Create new refresh token
        RefreshToken newToken = refreshTokenService.create(user);

        // 4. Generate new access token
        String accessToken = jwtUtil.generateToken(
                user.getId(),
                user.getRole().name()
        );

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newToken.getToken())
                .build();
    }

    public void logout(String accessToken,String refreshToken){
        Long userIdFromAccess= jwtUtil.extractUserId(accessToken);
        if(refreshToken!=null) {
            RefreshToken token = tokenRepo.findByToken(refreshToken)
                    .orElseThrow(() -> new ApiException("Invalid token", HttpStatus.UNAUTHORIZED));
            Long userIdFromRefresh = token.getUser().getId();
            if (!userIdFromAccess.equals(userIdFromRefresh)) {
                throw new ApiException("Token Mismatch", HttpStatus.UNAUTHORIZED);
            }
            token.setRevoked(true);
            tokenRepo.save(token);
        }
        // even if already revoked → no problem
        long expiry = jwtUtil.getExpirationMillis(accessToken);
        tokenBlacklistService.blacklistToken(accessToken, expiry);
    }
}
