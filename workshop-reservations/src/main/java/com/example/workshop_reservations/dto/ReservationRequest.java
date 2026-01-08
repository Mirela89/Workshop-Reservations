package com.example.workshop_reservations.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {

    @NotNull
    private Long workshopId;

    @NotNull
    private Long userId;

    @NotNull
    @Min(1)
    @Max(50)
    private Integer seats;
}
