package com.CommerceCore.service;

import com.CommerceCore.dto.UserDto;
import com.CommerceCore.entity.Role;
import com.CommerceCore.entity.User;
import com.CommerceCore.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    // Create User
    public UserDto createUser(UserDto dto){
        User user=mapToEntity(dto);
        return mapToDto(userRepo.save(user));
    }

    //Get User By id
    public UserDto getUserById(Long userId){
        User user=userRepo.findById(userId).orElseThrow(()->new RuntimeException("User Not Found"));
        return mapToDto(user);
    }

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
