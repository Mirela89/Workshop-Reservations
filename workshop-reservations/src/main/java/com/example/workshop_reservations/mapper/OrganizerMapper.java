package com.example.workshop_reservations.mapper;

import com.example.workshop_reservations.dto.OrganizerRequest;
import com.example.workshop_reservations.dto.OrganizerResponse;
import com.example.workshop_reservations.model.Organizer;
import org.springframework.stereotype.Component;

@Component
public class OrganizerMapper {

    // Convert OrganizerRequest DTO to Organizer entity
    public Organizer toEntity(OrganizerRequest request) {
        return Organizer.builder()
                .name(request.getName())
                .contactInfo(request.getContactInfo())
                .build();
    }

    // Update existing Organizer entity with data from OrganizerRequest DTO
    public void updateEntity(Organizer organizer, OrganizerRequest request) {
        organizer.setName(request.getName());
        organizer.setContactInfo(request.getContactInfo());
    }

    // Convert Organizer entity to OrganizerResponse DTO
    public OrganizerResponse toResponse(Organizer organizer) {
        return OrganizerResponse.builder()
                .id(organizer.getId())
                .name(organizer.getName())
                .contactInfo(organizer.getContactInfo())
                .createdAt(organizer.getCreatedAt())
                .build();
    }
}