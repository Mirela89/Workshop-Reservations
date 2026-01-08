package com.example.workshop_reservations.mapper;

import com.example.workshop_reservations.dto.UserRequest;
import com.example.workshop_reservations.dto.UserResponse;
import com.example.workshop_reservations.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    // Converts UserRequest DTO to User entity
    public User toEntity(UserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // Converts User entity to UserResponse DTO
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }
}