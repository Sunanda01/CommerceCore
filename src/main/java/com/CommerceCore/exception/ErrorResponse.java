package com.CommerceCore.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ErrorResponse {
    private String message;
    private HttpStatus status;
    private LocalDateTime timeStamp;
    private Map<String, String> errors;
}
