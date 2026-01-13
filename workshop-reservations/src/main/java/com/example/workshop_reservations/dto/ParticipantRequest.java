package com.example.workshop_reservations.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ParticipantRequest {
    @NotBlank
    private String fullName;

    @NotBlank
    @Email
    private String email;
}