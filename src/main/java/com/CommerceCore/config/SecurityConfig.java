package com.CommerceCore.config;

import com.CommerceCore.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth
                // Auth APIs
                .requestMatchers("/api/auth/**").permitAll()

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
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
