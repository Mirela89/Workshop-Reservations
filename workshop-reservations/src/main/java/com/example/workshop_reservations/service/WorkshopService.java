package com.example.workshop_reservations.service;

import com.example.workshop_reservations.dto.CreateWorkshopRequest;
import com.example.workshop_reservations.dto.UpdateWorkshopRequest;
import com.example.workshop_reservations.dto.WorkshopResponse;
import com.example.workshop_reservations.exception.ResourceNotFoundException;
import com.example.workshop_reservations.mapper.WorkshopMapper;
import com.example.workshop_reservations.model.Workshop;
import com.example.workshop_reservations.repository.WorkshopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final WorkshopMapper workshopMapper;

    // Create a new workshop
    public WorkshopResponse create(CreateWorkshopRequest request) {
        Workshop workshop = workshopMapper.toEntity(request);
        Workshop saved = workshopRepository.save(workshop);
        return workshopMapper.toResponse(saved);
    }

    // Retrieve all workshops
    public List<WorkshopResponse> getAll() {
        return workshopRepository.findAll().stream()
                .map(workshopMapper::toResponse)
                .toList();
    }

    // Retrieve a workshop by ID
    public WorkshopResponse getById(Long id) {
        Workshop workshop = workshopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop with id " + id + " not found"));
        return workshopMapper.toResponse(workshop);
    }

    // Update an existing workshop
    public WorkshopResponse update(Long id, UpdateWorkshopRequest request) {
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