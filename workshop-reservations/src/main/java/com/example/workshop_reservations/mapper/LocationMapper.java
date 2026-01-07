package com.example.workshop_reservations.mapper;

import com.example.workshop_reservations.dto.LocationRequest;
import com.example.workshop_reservations.dto.LocationResponse;
import com.example.workshop_reservations.model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    // Convert LocationRequest DTO to Location entity
    public Location toEntity(LocationRequest request) {
        return Location.builder()
                .name(request.getName())
                .address(request.getAddress())
                .build();
    }

    // Update existing Location entity with data from LocationRequest DTO
    public void updateEntity(Location location, LocationRequest request) {
        location.setName(request.getName());
        location.setAddress(request.getAddress());
    }

    // Convert Location entity to LocationResponse DTO
    public LocationResponse toResponse(Location location) {
        return LocationResponse.builder()
                .id(location.getId())
                .name(location.getName())
                .address(location.getAddress())
                .createdAt(location.getCreatedAt())
                .build();
    }
}