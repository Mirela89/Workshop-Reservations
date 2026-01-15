package com.example.workshop_reservations.dto;

import com.example.workshop_reservations.model.WorkshopStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkshopResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime date;
    private Integer capacity;
    private WorkshopStatus status;

    private Integer reservedSeats;
    private Integer availableSeats;

    private Long categoryId;
    private String categoryName;

    private Long locationId;
    private String locationName;

    private Long organizerId;
    private String organizerName;
}