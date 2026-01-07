package com.example.workshop_reservations.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkshopRequest {

    // Titlul/numele workshop-ului
    @NotBlank
    @Size(max = 100)
    private String title;

    // Descrierea workshop-ului
    @Size(max = 1000)
    private String description;

    // Data si ora desfasurarii workshop-ului
    @NotNull
    @Future
    private LocalDateTime date;

    // Capacitatea maxima a workshop-ului
    @NotNull
    @Min(1)
    @Max(500)
    private Integer capacity;
}
