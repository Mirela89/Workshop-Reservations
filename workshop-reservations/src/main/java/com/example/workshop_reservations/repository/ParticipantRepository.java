package com.example.workshop_reservations.repository;

import com.example.workshop_reservations.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    // Find all participants associated with a specific reservation
    List<Participant> findByReservationId(Long reservationId);

    // Find all participants associated with reservations for a specific workshop
    List<Participant> findByReservationWorkshopId(Long workshopId);

    // Find a participant by its ID and associated reservation ID
    Optional<Participant> findByIdAndReservationId(Long id, Long reservationId);

    // Check if a participant exists by its ID and associated reservation ID
    boolean existsByIdAndReservationId(Long id, Long reservationId);
}