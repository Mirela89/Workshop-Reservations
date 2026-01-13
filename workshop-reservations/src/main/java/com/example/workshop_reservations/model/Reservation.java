package com.example.workshop_reservations.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // Relatia Many-to-One cu User ( Many reservations to One User )
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relatia Many-to-One cu Workshop ( Many reservations to One Workshop )
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;

    // Relatia One-to-Many cu Participant ( One reservation to Many participants )
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();
}
