package com.example.workshop_reservations.controller;

import com.example.workshop_reservations.dto.OrganizerRequest;
import com.example.workshop_reservations.dto.OrganizerResponse;
import com.example.workshop_reservations.service.OrganizerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrganizerController.class)
class OrganizerControllerTest {

    @Autowired  private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private OrganizerService organizerService;

    @Test
    void shouldCreateOrganizer() throws Exception {
        // Request payload
        OrganizerRequest request = new OrganizerRequest();
        request.setName("Tech Community");
        request.setContactInfo("contact@tech.com");

        // Expected response
        OrganizerResponse response = OrganizerResponse.builder()
                .id(1L)
                .name("Tech Community")
                .contactInfo("contact@tech.com")
                .build();

        // Mocking the service layer
        Mockito.when(organizerService.create(Mockito.any()))
                .thenReturn(response);

        // Perform POST request and verify response
        mockMvc.perform(post("/organizers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Tech Community"))
                .andExpect(jsonPath("$.contactInfo").value("contact@tech.com"));
    }


    @Test
    void shouldGetAllOrganizers() throws Exception {
        OrganizerResponse o1 = OrganizerResponse.builder()
                .id(1L)
                .name("Tech Community")
                .build();

        OrganizerResponse o2 = OrganizerResponse.builder()
                .id(2L)
                .name("Java Group")
                .build();

        Mockito.when(organizerService.getAll())
                .thenReturn(List.of(o1, o2));

        mockMvc.perform(get("/organizers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Tech Community"))
                .andExpect(jsonPath("$[1].name").value("Java Group"));
    }


    @Test
    void shouldGetOrganizerById() throws Exception {
        OrganizerResponse response = OrganizerResponse.builder()
                .id(1L)
                .name("Tech Community")
                .build();

        Mockito.when(organizerService.getById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/organizers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Tech Community"));
    }


    @Test
    void shouldUpdateOrganizer() throws Exception {
        OrganizerRequest request = new OrganizerRequest();
        request.setName("Updated Organizer");
        request.setContactInfo("contact@techcommunity.com");

        OrganizerResponse response = OrganizerResponse.builder()
                .id(1L)
                .name("Updated Organizer")
                .build();

        Mockito.when(organizerService.update(Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(put("/organizers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Organizer"));
    }


    @Test
    void shouldDeleteOrganizer() throws Exception {
        Mockito.doNothing().when(organizerService).delete(1L);

        mockMvc.perform(delete("/organizers/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}