package com.example.workshop_reservations.service;


import com.example.workshop_reservations.dto.ReservationRequest;
import com.example.workshop_reservations.dto.ReservationResponse;
import com.example.workshop_reservations.exception.ResourceNotFoundException;
import com.example.workshop_reservations.mapper.ReservationMapper;
import com.example.workshop_reservations.model.*;
import com.example.workshop_reservations.repository.ReservationRepository;
import com.example.workshop_reservations.repository.UserRepository;
import com.example.workshop_reservations.repository.WorkshopRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final WorkshopRepository workshopRepository;
    private final ReservationMapper reservationMapper;
    private final UserRepository userRepository;

    // Create a new reservation
    @Transactional
    public ReservationResponse create(ReservationRequest request) {
        // Lock workshop row to avoid overselling in concurrent requests
        Workshop workshop = workshopRepository.findByIdForUpdate(request.getWorkshopId())
                .orElseThrow(() -> new ResourceNotFoundException("Workshop with id " + request.getWorkshopId() + " not found"));

        // Do not allow reservations for canceled workshops
        if (workshop.getStatus() == WorkshopStatus.CANCELED) {
            throw new IllegalArgumentException("Cannot create reservation. Workshop is canceled.");
        }

        // Do not allow reservations for workshops in the past
        if (workshop.getDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot create reservation for a workshop in the past.");
        }

        // Load user (user must exist)
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id " + request.getUserId() + " not found"));

        // Allow only one ACTIVE reservation per email for the same workshop
        if (reservationRepository.existsByWorkshopIdAndUserIdAndStatus(
                workshop.getId(), user.getId(), ReservationStatus.CREATED)) {
            throw new IllegalArgumentException("You already have an active reservation for this workshop.");
        }

        int reservedSeats = reservationRepository.sumSeatsByWorkshopAndStatus(workshop.getId(), ReservationStatus.CREATED);
        int availableSeats = workshop.getCapacity() - reservedSeats;

        if (request.getSeats() > availableSeats) {
            throw new IllegalArgumentException("Not enough available seats. Available: " + availableSeats);
        }

        Reservation reservation = reservationMapper.toEntity(request, workshop, user);

        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toResponse(saved);
    }

    // Retrieve a reservation by ID
    public ReservationResponse getById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation with id " + id + " not found"));
        return reservationMapper.toResponse(reservation);
    }

    // Retrieve all reservations for a specific workshop
    public List<ReservationResponse> getByWorkshop(Long workshopId) {
        // Ensure the workshop exists
        if (!workshopRepository.existsById(workshopId)) {
            throw new ResourceNotFoundException("Workshop with id " + workshopId + " not found");
        }

        return reservationRepository.findByWorkshopId(workshopId).stream()
                .map(reservationMapper::toResponse)
                .toList();
    }

    // Cancel a reservation
    @Transactional
    public ReservationResponse cancel(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation with id " + id + " not found"));

        // Do not allow cancel after the workshop has started
        if (reservation.getWorkshop().getDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot cancel reservation after the workshop has started.");
        }

        // If already canceled, return as is
        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            return reservationMapper.toResponse(reservation);
        }

        // Mark reservation as canceled
        reservation.setStatus(ReservationStatus.CANCELED);
        Reservation saved = reservationRepository.save(reservation);

        return reservationMapper.toResponse(saved);
    }
}
