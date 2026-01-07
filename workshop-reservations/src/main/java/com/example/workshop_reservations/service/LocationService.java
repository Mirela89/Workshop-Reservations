package com.example.workshop_reservations.service;

import com.example.workshop_reservations.dto.LocationRequest;
import com.example.workshop_reservations.dto.LocationResponse;
import com.example.workshop_reservations.exception.ResourceNotFoundException;
import com.example.workshop_reservations.mapper.LocationMapper;
import com.example.workshop_reservations.model.Location;
import com.example.workshop_reservations.repository.LocationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    // Create a new location
    @Transactional
    public LocationResponse create(LocationRequest request) {
        Location saved = locationRepository.save(locationMapper.toEntity(request));
        return locationMapper.toResponse(saved);
    }

    // Get all locations
    public List<LocationResponse> getAll() {
        return locationRepository.findAll().stream()
                .map(locationMapper::toResponse)
                .toList();
    }

    // Get location by id
    public LocationResponse getById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location with id " + id + " not found"));
        return locationMapper.toResponse(location);
    }

    // Update location
    @Transactional
    public LocationResponse update(Long id, LocationRequest request) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location with id " + id + " not found"));

        locationMapper.updateEntity(location, request);
        Location saved = locationRepository.save(location);
        return locationMapper.toResponse(saved);
    }

    // Delete location
    @Transactional
    public void delete(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Location with id " + id + " not found");
        }
        locationRepository.deleteById(id);
    }
}