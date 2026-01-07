package com.example.workshop_reservations.mapper;

import com.example.workshop_reservations.dto.ReservationResponse;
import com.example.workshop_reservations.model.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

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