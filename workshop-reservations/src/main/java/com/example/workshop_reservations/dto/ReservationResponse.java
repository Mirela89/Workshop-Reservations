package com.example.workshop_reservations.dto;

import com.example.workshop_reservations.model.ReservationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {
    private Long id;

    private Long workshopId;
    private String workshopTitle;

    private String fullName;
    private String email;
    private Integer seats;

    private Long userId;
    private String userName;
    private String userEmail;

    private ReservationStatus status;
    private LocalDateTime createdAt;
}
