package com.CommerceCore.service;

import com.CommerceCore.dto.UserDto;
import com.CommerceCore.entity.Role;
import com.CommerceCore.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    // Entity => DTO
    public UserDto mapToDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    // DTO => Entity
    public User mapToEntity(UserDto dto){
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .role(Role.USER)        // default role
                .build();
    }
}
