package com.CommerceCore.util;

import com.CommerceCore.exception.ApiException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    public String extractRefreshToken(HttpServletRequest request) {
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

    public void setRefreshCookie(HttpServletResponse response, String token){
        Cookie cookie=new Cookie("refreshToken",token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7*24*60*60);
        response.addCookie(cookie);
    }

    public void clearCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
