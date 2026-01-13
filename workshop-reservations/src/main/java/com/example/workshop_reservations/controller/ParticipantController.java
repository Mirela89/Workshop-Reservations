package com.example.workshop_reservations.controller;

import com.example.workshop_reservations.dto.ParticipantRequest;
import com.example.workshop_reservations.dto.ParticipantResponse;
import com.example.workshop_reservations.service.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Participant", description = "Operations for viewing reservation participants.")
@RestController
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    // Get participants by reservation ID
    // GET /reservations/{reservationId}/participants
    @Operation(
            summary = "Get participants for a reservation",
            description = "Returns all participants that belong to the given reservation."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participants returned successfully"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @GetMapping("/reservations/{reservationId}/participants")
    public List<ParticipantResponse> getByReservation(@PathVariable Long reservationId) {
        return participantService.getByReservation(reservationId);
    }

    // Get participants by workshop ID
    // GET /workshops/{workshopId}/participants
    @Operation(
            summary = "Get participants for a workshop",
            description = "Returns all participants from all reservations for the given workshop."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participants returned successfully"),
            @ApiResponse(responseCode = "404", description = "Workshop not found")
    })
    @GetMapping("/workshops/{workshopId}/participants")
    public List<ParticipantResponse> getByWorkshop(@PathVariable Long workshopId) {
        return participantService.getByWorkshop(workshopId);
    }

    // Update a participant
    // PUT /reservations/{reservationId}/participants/{participantId}
    @Operation(
            summary = "Update a participant",
            description = "Updates the details of a participant for a specific reservation."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participant updated successfully"),
            @ApiResponse(responseCode = "404", description = "Reservation or Participant not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/reservations/{reservationId}/participants/{participantId}")
    public ParticipantResponse update(@PathVariable Long reservationId,
                                      @PathVariable Long participantId,
                                      @Valid @RequestBody ParticipantRequest request) {
        return participantService.update(reservationId, participantId, request);
    }
}