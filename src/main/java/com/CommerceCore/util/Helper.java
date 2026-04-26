package com.CommerceCore.util;

import com.CommerceCore.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
@Component
public class Helper {
    @Autowired
    private ObjectMapper mapper;

    public void sendError(HttpServletResponse response,String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        ErrorResponse error = ErrorResponse.builder()
                .message(message)
                .status(HttpStatus.UNAUTHORIZED)
                .timeStamp(LocalDateTime.now())
                .build();
        mapper.writeValue(response.getWriter(), error);
    }
}
