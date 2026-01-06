package com.example.workshop_reservations.controller;

import com.example.workshop_reservations.dto.CreateWorkshopRequest;
import com.example.workshop_reservations.dto.UpdateWorkshopRequest;
import com.example.workshop_reservations.dto.WorkshopResponse;
import com.example.workshop_reservations.service.WorkshopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workshops")
@RequiredArgsConstructor
public class WorkshopController {

    private final WorkshopService workshopService;

    // Create a new workshop
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkshopResponse create(@Valid @RequestBody CreateWorkshopRequest request) {
        return workshopService.create(request);
    }

    // Retrieve all workshops
    @GetMapping
    public List<WorkshopResponse> getAll() {
        return workshopService.getAll();
    }

    // Retrieve a workshop by ID
    @GetMapping("/{id}")
    public WorkshopResponse getById(@PathVariable Long id) {
        return workshopService.getById(id);
    }

    // Update an existing workshop
    @PutMapping("/{id}")
    public WorkshopResponse update(@PathVariable Long id,
                                   @Valid @RequestBody UpdateWorkshopRequest request) {
        return workshopService.update(id, request);
    }

    // Delete a workshop by ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        workshopService.delete(id);
    }
}