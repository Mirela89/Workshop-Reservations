package com.example.workshop_reservations.service;

import com.example.workshop_reservations.dto.ParticipantRequest;
import com.example.workshop_reservations.dto.ParticipantResponse;
import com.example.workshop_reservations.exception.ResourceNotFoundException;
import com.example.workshop_reservations.mapper.ParticipantMapper;
import com.example.workshop_reservations.model.Participant;
import com.example.workshop_reservations.repository.ParticipantRepository;
import com.example.workshop_reservations.repository.ReservationRepository;
import com.example.workshop_reservations.repository.WorkshopRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final ReservationRepository reservationRepository;
    private final WorkshopRepository workshopRepository;
    private final ParticipantMapper participantMapper;

    // Retrieves participants by reservation ID
    public List<ParticipantResponse> getByReservation(Long reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new ResourceNotFoundException("Reservation with id " + reservationId + " not found");
        }

        return participantRepository.findByReservationId(reservationId).stream()
                .map(participantMapper::toResponse)
                .toList();
    }

    // Retrieves participants by workshop ID
    public List<ParticipantResponse> getByWorkshop(Long workshopId) {
        if (!workshopRepository.existsById(workshopId)) {
            throw new ResourceNotFoundException("Workshop with id " + workshopId + " not found");
        }

        return participantRepository.findByReservationWorkshopId(workshopId).stream()
                .map(participantMapper::toResponse)
                .toList();
    }

    // Updates a participant's details
    @Transactional
    public ParticipantResponse update(Long reservationId, Long participantId, ParticipantRequest request) {
        Participant participant = participantRepository.findByIdAndReservationId(participantId, reservationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Participant with id " + participantId + " not found for reservation " + reservationId));

        participantMapper.updateEntity(participant, request);
        // save updated participant
        Participant saved = participantRepository.save(participant);

        return participantMapper.toResponse(saved);
    }
}