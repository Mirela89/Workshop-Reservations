package com.example.workshop_reservations.mapper;

import com.example.workshop_reservations.dto.ParticipantRequest;
import com.example.workshop_reservations.dto.ParticipantResponse;
import com.example.workshop_reservations.model.Participant;
import com.example.workshop_reservations.model.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ParticipantMapper {

    // Converts a list of ParticipantRequest DTOs to a list of Participant entities
    public List<Participant> toEntity(List<ParticipantRequest> requests, Reservation reservation) {
        return requests.stream()
                .map(r -> Participant.builder()
                        .reservation(reservation)
                        .fullName(r.getFullName())
                        .email(r.getEmail())
                        .createdAt(LocalDateTime.now())
                        .build())
                .toList();
    }

    // Converts a Participant entity to a ParticipantResponse DTO
    public ParticipantResponse toResponse(Participant p) {
        return ParticipantResponse.builder()
                .id(p.getId())
                .fullName(p.getFullName())
                .email(p.getEmail())
                .reservationId(p.getReservation().getId())
                .workshopId(p.getReservation().getWorkshop().getId())
                .build();
    }

    // Updates an existing Participant entity with data from a ParticipantRequest DTO
    public void updateEntity(Participant p, ParticipantRequest req) {
        p.setFullName(req.getFullName());
        p.setEmail(req.getEmail());
    }
}