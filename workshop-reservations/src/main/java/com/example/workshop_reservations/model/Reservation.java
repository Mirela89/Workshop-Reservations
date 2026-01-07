package com.example.workshop_reservations.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Numele participantului
    @Column(nullable = false, length = 100)
    private String fullName;

    // Email-ul participantului
    @Column(nullable = false, length = 120)
    private String email;

    // Numarul de locuri rezervate la workshop
    @Column(nullable = false)
    private Integer seats;

    // Data si ora crearii rezervarii
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Statusul rezervarii
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    // Relatia Many-to-One cu Workshop ( Many reservations to One Workshop )
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;
}
