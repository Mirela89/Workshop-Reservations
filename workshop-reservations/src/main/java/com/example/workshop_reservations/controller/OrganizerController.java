package com.example.workshop_reservations.controller;

import com.example.workshop_reservations.dto.OrganizerRequest;
import com.example.workshop_reservations.dto.OrganizerResponse;
import com.example.workshop_reservations.service.OrganizerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Organizer", description = "Operations for managing workshop organizers.")
@RestController
@RequestMapping("/organizers")
@RequiredArgsConstructor
public class OrganizerController {

    private final OrganizerService organizerService;

    // Create organizer
    @Operation(summary = "Create organizer", description = "Creates a new organizer (name should be unique).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Organizer created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or organizer name already exists")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrganizerResponse create(@Valid @RequestBody OrganizerRequest request) {
        return organizerService.create(request);
    }

    // Get all organizers
    @Operation(summary = "Get all organizers", description = "Returns a list of all organizers.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Organizers retrieved successfully")
    })
    @GetMapping
    public List<OrganizerResponse> getAll() {
        return organizerService.getAll();
    }

    // Get organizer by id
    @Operation(summary = "Get organizer by id", description = "Returns organizer details by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Organizer found"),
            @ApiResponse(responseCode = "404", description = "Organizer not found")
    })
    @GetMapping("/{id}")
    public OrganizerResponse getById(@PathVariable Long id) {
        return organizerService.getById(id);
    }

    // Update organizer
    @Operation(summary = "Update organizer", description = "Updates an existing organizer by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Organizer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or organizer name already exists"),
            @ApiResponse(responseCode = "404", description = "Organizer not found")
    })
    @PutMapping("/{id}")
    public OrganizerResponse update(@PathVariable Long id, @Valid @RequestBody OrganizerRequest request) {
        return organizerService.update(id, request);
    }

    // Delete organizer
    @Operation(summary = "Delete organizer", description = "Deletes an organizer by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Organizer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Organizer not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        organizerService.delete(id);
    }
}