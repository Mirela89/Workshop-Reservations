package com.example.workshop_reservations.service;

import com.example.workshop_reservations.dto.WorkshopRequest;
import com.example.workshop_reservations.dto.WorkshopResponse;
import com.example.workshop_reservations.exception.ResourceNotFoundException;
import com.example.workshop_reservations.mapper.WorkshopMapper;
import com.example.workshop_reservations.model.*;
import com.example.workshop_reservations.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final WorkshopMapper workshopMapper;

    private final ReservationRepository reservationRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final OrganizerRepository organizerRepository;

    // Create a new workshop
    public WorkshopResponse create(WorkshopRequest request) {

        // Validate related entities
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + request.getCategoryId() + " not found"));

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location with id " + request.getLocationId() + " not found"));

        Organizer organizer = organizerRepository.findById(request.getOrganizerId())
                .orElseThrow(() -> new ResourceNotFoundException("Organizer with id " + request.getOrganizerId() + " not found"));

        // Create entity from request
        Workshop workshop = workshopMapper.toEntity(request);

        // Set related entities
        workshop.setCategory(category);
        workshop.setLocation(location);
        workshop.setOrganizer(organizer);

        // Save to repository
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

        // Validate related entities
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id " + request.getCategoryId() + " not found"));

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Location with id " + request.getLocationId() + " not found"));

        Organizer organizer = organizerRepository.findById(request.getOrganizerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Organizer with id " + request.getOrganizerId() + " not found"));

        // Set related entities
        workshop.setCategory(category);
        workshop.setLocation(location);
        workshop.setOrganizer(organizer);

        // Save updated entity
        Workshop saved = workshopRepository.save(workshop);
        return workshopMapper.toResponse(saved);
    }

    @Transactional
    public void cancelWorkshop(Long id) {
        Workshop workshop = workshopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop with id " + id + " not found"));

        // If already canceled, do nothing
        if (workshop.getStatus() == WorkshopStatus.CANCELED) {
            return;
        }

        workshop.setStatus(WorkshopStatus.CANCELED);
        workshopRepository.save(workshop);

        // Cancel all associated reservations
        reservationRepository.cancelByWorkshopId(id);
    }

    // Delete a workshop by ID
    public void delete(Long id) {
        if (!workshopRepository.existsById(id)) {
            throw new ResourceNotFoundException("Workshop with id " + id + " not found");
        }
        workshopRepository.deleteById(id);
    }
}