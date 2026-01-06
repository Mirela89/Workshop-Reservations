package com.example.workshop_reservations.mapper;

import com.example.workshop_reservations.dto.CreateWorkshopRequest;
import com.example.workshop_reservations.dto.UpdateWorkshopRequest;
import com.example.workshop_reservations.dto.WorkshopResponse;
import com.example.workshop_reservations.model.Workshop;
import org.springframework.stereotype.Component;

@Component
public class WorkshopMapper {

    // Converts Create DTO to Entity. Useful for POST /workshops.
    public Workshop toEntity(CreateWorkshopRequest dto) {
        return Workshop.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .date(dto.getDate())
                .capacity(dto.getCapacity())
                .build();
    }

    // Updates an existing entity with data from Update DTO. Useful for PUT /workshops/{id}.
    public void updateEntity(Workshop workshop, UpdateWorkshopRequest dto) {
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
                .build();
    }
}