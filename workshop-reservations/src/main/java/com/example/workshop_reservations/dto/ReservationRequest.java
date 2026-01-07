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

    @NotBlank
    @Size(max = 100)
    private String fullName;

    @NotBlank
    @Email
    @Size(max = 120)
    private String email;

    @NotNull
    @Min(1)
    @Max(50)
    private Integer seats;
}
