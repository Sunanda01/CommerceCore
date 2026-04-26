package com.CommerceCore.config;

import com.CommerceCore.Component.CustomOAuth2SuccessHandler;
import com.CommerceCore.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final CustomOAuth2SuccessHandler successHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth
                // Auth APIs
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/oAuth2").permitAll()

                // Admin APIs
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // User APIs
                .requestMatchers("/api/users/**").hasAnyRole("ADMIN","USER")

                // Product APIs
                .requestMatchers("/api/products/**").authenticated()

                // Order APIs
                .requestMatchers("/api/orders/**").authenticated()

                // Category APIs
                .requestMatchers("/api/categories/**").authenticated()

                // Cart APIs
                .requestMatchers("/api/carts/**").authenticated()

                // Fallback Apis
                .anyRequest().authenticated())
                .oauth2Login(oAuth -> oAuth.successHandler(successHandler))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
