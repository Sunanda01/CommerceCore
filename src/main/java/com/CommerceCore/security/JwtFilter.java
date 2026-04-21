package com.CommerceCore.security;

import com.CommerceCore.entity.CustomUserPrincipal;
import com.CommerceCore.entity.User;

import com.CommerceCore.exception.ErrorResponse;
import com.CommerceCore.helper.Helper;
import com.CommerceCore.repository.UserRepo;


import com.CommerceCore.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepo repo;
    private final TokenBlacklistService tokenBlacklistService;
    private final Helper helper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            final String authHeader= request.getHeader("Authorization");
            String jwt=null;
            Long userId=null;
//            String role=null;

            // Extract Token
            if(authHeader!=null && authHeader.startsWith("Bearer ")){
                jwt=authHeader.substring(7);
            }

            if(jwt==null){
                filterChain.doFilter(request,response);
                return;
            }

            if(tokenBlacklistService.isTokenBlacklisted(jwt)){
                helper.sendError(response,"Token is Blacklisted");
                return;
            }

            userId=jwtUtil.extractUserId(jwt);

            if(!jwtUtil.isTokenValid(jwt,userId)){
                helper.sendError(response,"Token is Invalid");
                return;
            }

            // validate & Set Authentication
            if(SecurityContextHolder.getContext().getAuthentication()==null){
                User user=repo.findById(userId).orElseThrow(()->new RuntimeException("User Not Found"));
                List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                );
                CustomUserPrincipal principal = new CustomUserPrincipal(
                        user.getId(),
                        user.getEmail(),
                        authorities
                );
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        principal.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request,response);
        } catch (Exception e) {
            helper.sendError(response,"Invalid or Expired Token");
        }

    }
}
