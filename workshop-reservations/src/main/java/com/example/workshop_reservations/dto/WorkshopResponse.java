package com.example.workshop_reservations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class WorkshopResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime date;
    private Integer capacity;

    private Integer reservedSeats;
    private Integer availableSeats;

    private Long categoryId;
    private String categoryName;

    private Long locationId;
    private String locationName;

    private Long organizerId;
    private String organizerName;
}