package com.CommerceCore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    @Email(message="Invalid Email Format")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Name is Required")
    @Size(min = 3, message = "Name must be at least 3 characters")
    private String name;
}
