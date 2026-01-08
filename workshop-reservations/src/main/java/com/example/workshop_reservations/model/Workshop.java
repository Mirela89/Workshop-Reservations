package com.example.workshop_reservations.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "workshops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workshop {

    // Cheia primara
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Titlul/Numele workshop-ului
    @Column(nullable = false, length = 100)
    private String title;

    // Descrierea workshop-ului
    @Column(length = 1000)
    private String description;

    // Data si ora desfasurarii workshop-ului
    @Column(nullable = false)
    private LocalDateTime date;

    // Capacitatea maxima a workshop-ului
    @Column(nullable = false)
    private Integer capacity;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkshopStatus status = WorkshopStatus.ACTIVE;

    // Relatii Many-to-One cu Category
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Relatii Many-to-One cu Location
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    // Relatii Many-to-One cu Organizer
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizer_id", nullable = false)
    private Organizer organizer;
}