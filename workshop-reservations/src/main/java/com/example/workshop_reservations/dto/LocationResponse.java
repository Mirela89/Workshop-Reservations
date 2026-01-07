package com.example.workshop_reservations.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationResponse {

    private Long id;
    private String name;
    private String address;
    private LocalDateTime createdAt;
}