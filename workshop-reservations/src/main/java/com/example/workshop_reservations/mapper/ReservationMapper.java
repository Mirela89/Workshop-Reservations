package com.example.workshop_reservations.mapper;

import com.example.workshop_reservations.dto.ParticipantResponse;
import com.example.workshop_reservations.dto.ReservationRequest;
import com.example.workshop_reservations.dto.ReservationResponse;
import com.example.workshop_reservations.model.Reservation;
import com.example.workshop_reservations.model.ReservationStatus;
import com.example.workshop_reservations.model.User;
import com.example.workshop_reservations.model.Workshop;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservationMapper {

    // Converts ReservationRequest DTO to Reservation entity
    public Reservation toEntity(ReservationRequest request, Workshop workshop, User user) {
        return Reservation.builder()
                .workshop(workshop)
                .user(user)
                .seats(request.getSeats())
                .createdAt(LocalDateTime.now())
                .status(ReservationStatus.CREATED)
                .build();
    }

    // Converts Reservation entity to ReservationResponse DTO
    public ReservationResponse toResponse(Reservation r) {
        return ReservationResponse.builder()
                .id(r.getId())
                // Workshop
                .workshopId(r.getWorkshop().getId())
                .workshopTitle(r.getWorkshop().getTitle())

                // User
                .userId(r.getUser().getId())
                .userName(r.getUser().getName())
                .userEmail(r.getUser().getEmail())

                // Reservation fields
                .seats(r.getSeats())
                .status(r.getStatus())
                .createdAt(r.getCreatedAt())

                // Participants
                .participants(
                        r.getParticipants().stream()
                                .map(p -> ParticipantResponse.builder()
                                        .id(p.getId())
                                        .fullName(p.getFullName())
                                        .email(p.getEmail())
                                        .reservationId(r.getId())
                                        .workshopId(r.getWorkshop().getId())
                                        .build())
                                .toList()
                )
                .build();
    }
}