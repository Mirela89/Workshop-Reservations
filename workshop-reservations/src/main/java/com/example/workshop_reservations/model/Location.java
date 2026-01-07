package com.example.workshop_reservations.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "locations")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    // Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Name of the location
    @Column(nullable = false, length = 120)
    private String name;

    // Address of the location
    @Column(nullable = false, length = 255)
    private String address;

    // Timestamp for when the location was created
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Method to set createdAt before persisting
    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}