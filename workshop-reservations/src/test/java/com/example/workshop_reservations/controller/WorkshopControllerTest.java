package com.example.workshop_reservations.controller;

import com.example.workshop_reservations.dto.WorkshopRequest;
import com.example.workshop_reservations.dto.WorkshopResponse;
import com.example.workshop_reservations.model.WorkshopStatus;
import com.example.workshop_reservations.service.WorkshopService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WorkshopController.class)
class WorkshopControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    //@MockBean private WorkshopService workshopService;
    @MockitoBean private WorkshopService workshopService;

    // Helper: build a valid request body.
    private WorkshopRequest validRequest() {
        WorkshopRequest req = new WorkshopRequest();
        req.setTitle("Intro to Spring Boot");
        req.setDescription("Workshop practic");
        req.setDate(LocalDateTime.parse("2026-02-01T10:00:00"));
        req.setCapacity(20);
        req.setCategoryId(1L);
        req.setLocationId(2L);
        req.setOrganizerId(3L);
        return req;
    }

    // Helper: build a sample response body
    private WorkshopResponse sampleResponse(Long id) {
        return WorkshopResponse.builder()
                .id(id)
                .title("Intro to Spring Boot")
                .description("Workshop practic")
                .date(LocalDateTime.parse("2026-02-01T10:00:00"))
                .capacity(20)
                .status(WorkshopStatus.ACTIVE)
                .reservedSeats(0)
                .availableSeats(20)
                .categoryId(1L)
                .categoryName("IT")
                .locationId(2L)
                .locationName("TechHub")
                .organizerId(3L)
                .organizerName("Tech Community")
                .build();
    }

    @Test
    void create_whenValid_shouldCreateWorkshop() throws Exception {
        WorkshopRequest req = validRequest();
        WorkshopResponse resp = sampleResponse(10L);

        when(workshopService.create(any(WorkshopRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/workshops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Intro to Spring Boot"));
    }

    @Test
    void create_whenInvalid_shouldFailValidation() throws Exception {
        // Missing required fields and invalid values
        String invalidJson = """
            {
              "title": "",
              "description": "x",
              "date": null,
              "capacity": null,
              "categoryId": null,
              "locationId": null,
              "organizerId": null
            }
            """;

        // Perform the request and expect 400 Bad Request
        mockMvc.perform(post("/workshops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_shouldReturnWorkshops() throws Exception {
        when(workshopService.getAll()).thenReturn(List.of(sampleResponse(1L), sampleResponse(2L)));

        mockMvc.perform(get("/workshops"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void getById_whenExists_shouldReturnWorkshop() throws Exception {
        when(workshopService.getById(7L)).thenReturn(sampleResponse(7L));

        mockMvc.perform(get("/workshops/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7));
    }

    @Test
    void update_whenValid_shouldUpdateWorkshop() throws Exception {
        WorkshopRequest req = validRequest();
        when(workshopService.update(eq(5L), any(WorkshopRequest.class))).thenReturn(sampleResponse(5L));

        mockMvc.perform(put("/workshops/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void cancel_whenExists_shouldCancelWorkshop() throws Exception {
        doNothing().when(workshopService).cancelWorkshop(3L);

        mockMvc.perform(post("/workshops/3/cancel"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_whenExists_shouldDeleteWorkshop() throws Exception {
        doNothing().when(workshopService).delete(9L);

        mockMvc.perform(delete("/workshops/9"))
                .andExpect(status().isNoContent());
    }
}