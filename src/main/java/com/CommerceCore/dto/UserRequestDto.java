package com.CommerceCore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {
    private Long id;
    private String email;
    private String name;
    private String password;
}
