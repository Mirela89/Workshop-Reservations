package com.example.workshop_reservations.service;

import com.example.workshop_reservations.dto.UserRequest;
import com.example.workshop_reservations.dto.UserResponse;
import com.example.workshop_reservations.exception.ResourceNotFoundException;
import com.example.workshop_reservations.mapper.UserMapper;
import com.example.workshop_reservations.model.User;
import com.example.workshop_reservations.repository.ReservationRepository;
import com.example.workshop_reservations.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final UserMapper userMapper;

    // Create user
    public UserResponse create(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User saved = userRepository.save(userMapper.toEntity(request));
        return userMapper.toResponse(saved);
    }

    // Get user by ID
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        return userMapper.toResponse(user);
    }

    // Delete user
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }

        // Check for existing reservations
        if (reservationRepository.existsByUserId(id)) {
            throw new IllegalArgumentException("Cannot delete user with existing reservations");
        }

        userRepository.deleteById(id);
    }
}