package com.example.workshop_reservations.controller;

import com.example.workshop_reservations.dto.ReservationRequest;
import com.example.workshop_reservations.dto.ReservationResponse;
import com.example.workshop_reservations.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Reservation", description = "Operations for creating and canceling workshop reservations.")
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // Create a new reservation
    // POST /reservations
    @Operation(
            summary = "Create a reservation",
            description = "Creates a reservation for a workshop if enough seats are available."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reservation created successfully"),
            @ApiResponse(responseCode = "400", description = "Not enough available seats or validation error"),
            @ApiResponse(responseCode = "404", description = "Workshop not found")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse create(@Valid @RequestBody ReservationRequest request) {
        return reservationService.create(request);
    }

    // Retrieve a reservation by ID
    // GET /reservations/{id}
    @Operation(
            summary = "Get reservation by id",
            description = "Returns details of a reservation by its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservation found"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @GetMapping("/{id}")
    public ReservationResponse getById(@PathVariable Long id) {
        return reservationService.getById(id);
    }

    // Retrieve all reservations for a specific workshop
    // GET /workshops/{workshopId}/reservations
    @Operation(
            summary = "Get reservations for a workshop",
            description = """
                    Returns all reservations for a given workshop.
                    Includes both CREATED and CANCELED reservations.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Workshop not found")
    })
    @GetMapping("/workshops/{workshopId}/reservations")
    public List<ReservationResponse> getByWorkshop(@PathVariable Long workshopId) {
        return reservationService.getByWorkshop(workshopId);
    }

    // Cancel a reservation
    // POST /reservations/{id}/cancel
    @Operation(
            summary = "Cancel a reservation",
            description = "Cancels an existing reservation by its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservation canceled successfully"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @PostMapping("/{id}/cancel")
    public ReservationResponse cancel(@PathVariable Long id) {
        return reservationService.cancel(id);
    }
}
