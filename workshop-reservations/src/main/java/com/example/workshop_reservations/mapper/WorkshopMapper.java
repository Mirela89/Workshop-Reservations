package com.example.workshop_reservations.mapper;

import com.example.workshop_reservations.dto.WorkshopRequest;
import com.example.workshop_reservations.dto.WorkshopResponse;
import com.example.workshop_reservations.model.Workshop;
import org.springframework.stereotype.Component;

@Component
public class WorkshopMapper {

    // Converts Create DTO to Entity. Useful for POST /workshops.
    public Workshop toEntity(WorkshopRequest dto) {
        return Workshop.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .date(dto.getDate())
                .capacity(dto.getCapacity())
                .build();
    }

    // Updates an existing entity with data from Update DTO. Useful for PUT /workshops/{id}.
    public void updateEntity(Workshop workshop, WorkshopRequest dto) {
        workshop.setTitle(dto.getTitle());
        workshop.setDescription(dto.getDescription());
        workshop.setDate(dto.getDate());
        workshop.setCapacity(dto.getCapacity());
    }

    // Converts Entity to Response DTO. Useful for all responses.
    public WorkshopResponse toResponse(Workshop workshop) {
        return WorkshopResponse.builder()
                .id(workshop.getId())
                .title(workshop.getTitle())
                .description(workshop.getDescription())
                .date(workshop.getDate())
                .capacity(workshop.getCapacity())
                .status(workshop.getStatus())
                .categoryId(workshop.getCategory().getId())
                .categoryName(workshop.getCategory().getName())
                .locationId(workshop.getLocation().getId())
                .locationName(workshop.getLocation().getName())
                .organizerId(workshop.getOrganizer().getId())
                .organizerName(workshop.getOrganizer().getName())
                .build();
    }
}