package com.example.workshop_reservations.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateWorkshopRequest {

    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 1000)
    private String description;

    @NotNull
    @Future
    private LocalDateTime date;

    @NotNull
    @Min(1)
    @Max(500)
    private Integer capacity;
}