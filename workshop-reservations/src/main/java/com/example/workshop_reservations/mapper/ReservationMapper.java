package com.example.workshop_reservations.mapper;

import com.example.workshop_reservations.dto.ReservationRequest;
import com.example.workshop_reservations.dto.ReservationResponse;
import com.example.workshop_reservations.model.Reservation;
import com.example.workshop_reservations.model.ReservationStatus;
import com.example.workshop_reservations.model.Workshop;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservationMapper {

    // Converts ReservationRequest DTO to Reservation entity
    public Reservation toEntity(ReservationRequest request, Workshop workshop) {
        return Reservation.builder()
                .workshop(workshop)
                .fullName(request.getFullName())
                .email(request.getEmail())
                .seats(request.getSeats())
                .createdAt(LocalDateTime.now())
                .status(ReservationStatus.CREATED)
                .build();
    }

    // Converts Reservation entity to ReservationResponse DTO
    public ReservationResponse toResponse(Reservation r) {
        return ReservationResponse.builder()
                .id(r.getId())
                .workshopId(r.getWorkshop().getId())
                .workshopTitle(r.getWorkshop().getTitle())
                .fullName(r.getFullName())
                .email(r.getEmail())
                .seats(r.getSeats())
                .status(r.getStatus())
                .createdAt(r.getCreatedAt())
                .build();
    }
}