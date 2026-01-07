package com.example.workshop_reservations.repository;

import com.example.workshop_reservations.model.Reservation;
import com.example.workshop_reservations.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Find all reservations for a specific workshop
    List<Reservation> findByWorkshopId(Long workshopId);

    // Sum of seats for a workshop with a specific reservation status
    @Query("""
           select coalesce(sum(r.seats), 0)
           from Reservation r
           where r.workshop.id = :workshopId
             and r.status = :status
           """)
    int sumSeatsByWorkshopAndStatus(Long workshopId, ReservationStatus status);

    // Check if a reservation exists for a workshop by email and status
    boolean existsByWorkshopIdAndEmailAndStatus(Long workshopId, String email, ReservationStatus status);
}