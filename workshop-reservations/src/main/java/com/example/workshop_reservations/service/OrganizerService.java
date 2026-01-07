package com.example.workshop_reservations.service;

import com.example.workshop_reservations.dto.OrganizerRequest;
import com.example.workshop_reservations.dto.OrganizerResponse;
import com.example.workshop_reservations.exception.ResourceNotFoundException;
import com.example.workshop_reservations.mapper.OrganizerMapper;
import com.example.workshop_reservations.model.Organizer;
import com.example.workshop_reservations.repository.OrganizerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizerService {

    private final OrganizerRepository organizerRepository;
    private final OrganizerMapper organizerMapper;

    // Create organizer
    @Transactional
    public OrganizerResponse create(OrganizerRequest request) {
        // Organizer name unique
        if (organizerRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Organizer name already exists: " + request.getName());
        }

        Organizer saved = organizerRepository.save(organizerMapper.toEntity(request));
        return organizerMapper.toResponse(saved);
    }

    // Get all organizers
    public List<OrganizerResponse> getAll() {
        return organizerRepository.findAll().stream()
                .map(organizerMapper::toResponse)
                .toList();
    }

    // Get organizer by id
    public OrganizerResponse getById(Long id) {
        Organizer organizer = organizerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organizer with id " + id + " not found"));
        return organizerMapper.toResponse(organizer);
    }

    // Update organizer
    @Transactional
    public OrganizerResponse update(Long id, OrganizerRequest request) {
        Organizer organizer = organizerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organizer with id " + id + " not found"));

        // Prevent duplicate names on update
        if (!organizer.getName().equalsIgnoreCase(request.getName())
                && organizerRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Organizer name already exists: " + request.getName());
        }

        organizerMapper.updateEntity(organizer, request);
        Organizer saved = organizerRepository.save(organizer);

        return organizerMapper.toResponse(saved);
    }

    // Delete organizer
    @Transactional
    public void delete(Long id) {
        if (!organizerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Organizer with id " + id + " not found");
        }
        organizerRepository.deleteById(id);
    }
}