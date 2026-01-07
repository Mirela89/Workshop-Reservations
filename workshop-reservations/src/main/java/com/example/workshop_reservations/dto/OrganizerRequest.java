package com.example.workshop_reservations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizerRequest {

    @NotBlank(message = "Organizer name is required")
    @Size(max = 120, message = "Organizer name must be at most 120 characters")
    private String name;

    @Size(max = 255, message = "Contact info must be at most 255 characters")
    private String contactInfo;
}