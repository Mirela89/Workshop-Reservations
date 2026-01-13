package com.example.workshop_reservations.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParticipantResponse {
    private Long id;
    private String fullName;
    private String email;

    private Long reservationId;
    private Long workshopId;
}