package com.example.workshop_reservations.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "organizers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organizer {

    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Organizer name
    @Column(nullable = false, length = 120)
    private String name;

    // Contact information
    @Column(name = "contact_info", length = 255)
    private String contactInfo;

    // Timestamp of creation
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Automatically set createdAt before persisting
    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}