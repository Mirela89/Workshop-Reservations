package com.example.workshop_reservations.controller;

import com.example.workshop_reservations.dto.LocationRequest;
import com.example.workshop_reservations.dto.LocationResponse;
import com.example.workshop_reservations.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Location", description = "Operations for managing workshop locations.")
@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    // Create Location
    @Operation(summary = "Create location", description = "Creates a new location with name and address.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Location created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationResponse create(@Valid @RequestBody LocationRequest request) {
        return locationService.create(request);
    }

    // Get All Locations
    @Operation(summary = "Get all locations", description = "Returns a list of all locations.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Locations retrieved successfully")
    })
    @GetMapping
    public List<LocationResponse> getAll() {
        return locationService.getAll();
    }

    // Get Location by ID
    @Operation(summary = "Get location by id", description = "Returns location details by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Location found"),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    @GetMapping("/{id}")
    public LocationResponse getById(@PathVariable Long id) {
        return locationService.getById(id);
    }

    // Update Location
    @Operation(summary = "Update location", description = "Updates an existing location by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Location updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    @PutMapping("/{id}")
    public LocationResponse update(@PathVariable Long id, @Valid @RequestBody LocationRequest request) {
        return locationService.update(id, request);
    }

    // Delete Location
    @Operation(summary = "Delete location", description = "Deletes a location by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Location deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Location not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        locationService.delete(id);
    }
}