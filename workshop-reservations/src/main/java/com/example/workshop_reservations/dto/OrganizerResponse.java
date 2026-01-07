package com.example.workshop_reservations.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizerResponse {

    private Long id;
    private String name;
    private String contactInfo;
    private LocalDateTime createdAt;
}