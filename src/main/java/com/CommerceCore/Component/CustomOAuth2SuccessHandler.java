    package com.CommerceCore.Component;

    import com.CommerceCore.controller.AuthController;
    import com.CommerceCore.entity.AuthProvider;
    import com.CommerceCore.entity.RefreshToken;
    import com.CommerceCore.entity.Role;
    import com.CommerceCore.entity.User;
    import com.CommerceCore.exception.ApiException;
    import com.CommerceCore.repository.UserRepo;
    import com.CommerceCore.security.JwtUtil;
    import com.CommerceCore.service.RefreshTokenService;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.oauth2.core.user.OAuth2User;
    import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
    import org.springframework.stereotype.Component;

    import java.io.IOException;

    @Component
    @RequiredArgsConstructor
    public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
        private final JwtUtil jwtUtil;
        private final RefreshTokenService refreshTokenService;
        private final UserRepo repo;
        private final CookieUtil cookieUtil;

        @Override
        public void onAuthenticationSuccess(
                HttpServletRequest request,
                HttpServletResponse response,
                Authentication authentication
        )throws IOException {
            OAuth2User oAuth2User=(OAuth2User) authentication.getPrincipal();
            String email=oAuth2User.getAttribute("email");
            String name=oAuth2User.getAttribute("name");

            // 1. Check if user exists
            User user=repo.findByEmail(email)
                    .map(existing->{
                        if(existing.getProvider()==AuthProvider.LOCAL){
                            throw new ApiException("User Already Registered With Email/Password. Use Normal Login", HttpStatus.BAD_REQUEST);
                        }
                        return existing;
                    })
                    .orElseGet(()->{
                        User newUser=User.builder()
                                .email(email)
                                .name(name)
                                .role(Role.USER)
                                .provider(AuthProvider.GOOGLE)
                                .build();
                        return repo.save(newUser);
                    });

            // 2. Generate JWT
            String accessToken= jwtUtil.generateToken(user.getId(), user.getRole().name());
            refreshTokenService.deleteByUser(user);
            RefreshToken refreshToken=refreshTokenService.create(user);
            cookieUtil.setRefreshCookie(response,refreshToken.getToken());

            // 3. Return token
            response.setContentType("application/json");
            response.getWriter().write(
                    "{ \"accessToken\": \"" + accessToken + "\", " +
                            "\"refreshToken\": \"" + refreshToken.getToken() + "\" }"
            );
        }
    }
