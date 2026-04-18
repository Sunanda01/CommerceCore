package com.CommerceCore.service;

import com.CommerceCore.dto.UserDto;
import com.CommerceCore.dto.UserRequestDto;
import com.CommerceCore.dto.UserResponseDto;
import com.CommerceCore.entity.Role;
import com.CommerceCore.entity.User;
import com.CommerceCore.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    // Create User
    public UserResponseDto createUser(UserRequestDto dto){
        if(userRepo.findByEmail(dto.getEmail()).isPresent()){
            throw new RuntimeException("User Already exists");
        }
        User user=mapToEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return mapToDto(userRepo.save(user));
    }

    //Get User By id
    public UserResponseDto getUserById(Long userId){
        User user=userRepo.findById(userId).orElseThrow(()->new RuntimeException("User Not Found"));
        return mapToDto(user);
    }

    // Entity => DTO
    public UserResponseDto mapToDto(User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    // DTO => Entity
    public User mapToEntity(UserRequestDto dto){
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(Role.USER)        // default role
                .build();
    }
}
