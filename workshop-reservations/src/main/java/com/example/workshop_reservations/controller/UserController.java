package com.example.workshop_reservations.controller;

import com.example.workshop_reservations.dto.UserRequest;
import com.example.workshop_reservations.dto.UserResponse;
import com.example.workshop_reservations.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User", description = "Operations for managing users")
public class UserController {

    private final UserService userService;

    // Create a new user
    // POST /users
    @Operation(
            summary = "Create user",
            description = "Creates a new user with the provided details."
    )
    @ApiResponses(
            @ApiResponse(responseCode = "201", description = "User created successfully")
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserRequest request) {
        return userService.create(request);
    }

    // Get user by ID
    // GET /users/{id}
    @Operation(
            summary = "Get user by ID",
            description = "Retrieves user details by their unique ID."
    )
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    )
    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    // Delete user by ID
    // DELETE /users/{id}
    @Operation(
            summary = "Delete user",
            description = "Deletes a user by their unique ID."
    )
    @ApiResponses(
            @ApiResponse(responseCode = "204", description = "User deleted successfully")
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}