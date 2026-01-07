package com.example.workshop_reservations.service;

import com.example.workshop_reservations.dto.WorkshopRequest;
import com.example.workshop_reservations.dto.WorkshopResponse;
import com.example.workshop_reservations.exception.ResourceNotFoundException;
import com.example.workshop_reservations.mapper.WorkshopMapper;
import com.example.workshop_reservations.model.ReservationStatus;
import com.example.workshop_reservations.model.Workshop;
import com.example.workshop_reservations.repository.ReservationRepository;
import com.example.workshop_reservations.repository.WorkshopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final WorkshopMapper workshopMapper;

    private final ReservationRepository reservationRepository;

    // Create a new workshop
    public WorkshopResponse create(WorkshopRequest request) {
        Workshop workshop = workshopMapper.toEntity(request);
        Workshop saved = workshopRepository.save(workshop);
        return workshopMapper.toResponse(saved);
    }

    // Retrieve all workshops
    public List<WorkshopResponse> getAll() {
        return workshopRepository.findAll().stream()
                .map(w -> {
                    int reserved = reservationRepository.sumSeatsByWorkshopAndStatus(w.getId(), ReservationStatus.CREATED);
                    int available = w.getCapacity() - reserved;

                    WorkshopResponse resp = workshopMapper.toResponse(w);
                    resp.setReservedSeats(reserved);
                    resp.setAvailableSeats(available);
                    return resp;
                })
                .toList();
    }

    // Retrieve a workshop by ID
    public WorkshopResponse getById(Long id) {
        Workshop workshop = workshopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop with id " + id + " not found"));

        int reserved = reservationRepository.sumSeatsByWorkshopAndStatus(id, ReservationStatus.CREATED);
        int available = workshop.getCapacity() - reserved;

        // mapare + setări extra
        WorkshopResponse response = workshopMapper.toResponse(workshop);
        response.setReservedSeats(reserved);
        response.setAvailableSeats(available);

        return response;
    }

    // Update an existing workshop
    public WorkshopResponse update(Long id, WorkshopRequest request) {
        Workshop workshop = workshopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop with id " + id + " not found"));

        workshopMapper.updateEntity(workshop, request);

        Workshop saved = workshopRepository.save(workshop);
        return workshopMapper.toResponse(saved);
    }

    // Delete a workshop by ID
    public void delete(Long id) {
        if (!workshopRepository.existsById(id)) {
            throw new ResourceNotFoundException("Workshop with id " + id + " not found");
        }
        workshopRepository.deleteById(id);
    }
}