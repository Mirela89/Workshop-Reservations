package com.example.workshop_reservations.controller;

import com.example.workshop_reservations.dto.WorkshopRequest;
import com.example.workshop_reservations.dto.WorkshopResponse;
import com.example.workshop_reservations.service.WorkshopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Workshop", description = "Operations for managing workshops.")
@RestController
@RequestMapping("/workshops")
@RequiredArgsConstructor
public class WorkshopController {

    private final WorkshopService workshopService;

    // Create a new workshop
    // POST /workshops
    @Operation(
            summary = "Create a new workshop",
            description = "Creates a workshop with necessary data."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Workshop created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body / validation error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkshopResponse create(@Valid @RequestBody WorkshopRequest request) {
        return workshopService.create(request);
    }

    // Retrieve all workshops
    // GET /workshops
    @Operation(
            summary = "Get all workshops",
            description = "Returns a list of all available workshops."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workshops retrieved successfully")
    })
    @GetMapping
    public List<WorkshopResponse> getAll() {
        return workshopService.getAll();
    }

    // Retrieve a workshop by ID
    // GET /workshops/{id}
    @Operation(
            summary = "Get workshop by id",
            description = "Returns workshop details by id. Useful for viewing details about each workshop."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workshop found"),
            @ApiResponse(responseCode = "404", description = "Workshop not found")
    })
    @GetMapping("/{id}")
    public WorkshopResponse getById(@PathVariable Long id) {
        return workshopService.getById(id);
    }

    // Update an existing workshop
    // PUT /workshops/{id}
    @Operation(
            summary = "Update workshop",
            description = "Updates an existing workshop (title, description, date, capacity, etc)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workshop updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body / validation error"),
            @ApiResponse(responseCode = "404", description = "Workshop not found")
    })
    @PutMapping("/{id}")
    public WorkshopResponse update(@PathVariable Long id,
                                   @Valid @RequestBody WorkshopRequest request) {
        return workshopService.update(id, request);
    }

    // Cancel a workshop by ID
    // POST /workshops/{id}/cancel
    @Operation(
            summary = "Cancel workshop",
            description = "Cancels a workshop by id. All existing reservations will also be cancelled."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Workshop cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Workshop not found")
    })
    @PostMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) {
        workshopService.cancelWorkshop(id);
    }

    // Delete a workshop by ID
    // DELETE /workshops/{id}
    @Operation(
            summary = "Delete workshop",
            description = "Deletes a workshop by id. After deletion, the workshop will no longer be available in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Workshop deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Workshop not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        workshopService.delete(id);
    }
}