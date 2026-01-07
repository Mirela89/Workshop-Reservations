package com.example.workshop_reservations.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    // Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Name of the category
    @Column(nullable = false, length = 80, unique = true)
    private String name;

    // Timestamp for when the category was created
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