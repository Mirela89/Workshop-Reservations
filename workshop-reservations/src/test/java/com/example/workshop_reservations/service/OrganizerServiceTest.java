package com.example.workshop_reservations.service;

import com.example.workshop_reservations.dto.OrganizerRequest;
import com.example.workshop_reservations.dto.OrganizerResponse;
import com.example.workshop_reservations.exception.ResourceNotFoundException;
import com.example.workshop_reservations.mapper.OrganizerMapper;
import com.example.workshop_reservations.model.Organizer;
import com.example.workshop_reservations.repository.OrganizerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizerServiceTest {

    // Mock dependencies
    @Mock
    private OrganizerRepository organizerRepository;

    @Mock
    private OrganizerMapper organizerMapper;

    // Create a real OrganizerService instance and inject the mocks above into it
    @InjectMocks
    private OrganizerService organizerService;

    // Test cases
    @Test
    @DisplayName("Create organizer - happy flow")
    void createOrganizerTest() {
        // Arrange (request DTO)
        OrganizerRequest request = new OrganizerRequest();
        request.setName("Tech Community");
        request.setContactInfo("contact@tech.com");

        // Arrange (entity to be saved)
        Organizer entity = new Organizer();
        entity.setName("Tech Community");
        entity.setContactInfo("contact@tech.com");

        // Arrange (simulate what repository returns after saving)
        Organizer saved = new Organizer();
        saved.setId(10L);
        saved.setName("Tech Community");
        saved.setContactInfo("contact@tech.com");

        // Arrange (expected response)
        OrganizerResponse response = new OrganizerResponse();
        response.setId(10L);
        response.setName("Tech Community");
        response.setContactInfo("contact@tech.com");

        // Stubbing (define mock behaviors used inside organizerService.create)
        when(organizerMapper.toEntity(request)).thenReturn(entity);
        when(organizerRepository.save(entity)).thenReturn(saved);
        when(organizerMapper.toResponse(saved)).thenReturn(response);

        // Act (call the method under test)
        OrganizerResponse result = organizerService.create(request);

        // Assert (verify the result)
        assertEquals(10L, result.getId());
        assertEquals("Tech Community", result.getName());
    }

    @Test
    @DisplayName("Get organizer by ID - happy flow")
    void getById_shouldReturnOne() {
        // Arrange (entity)
        Organizer entity = new Organizer();
        entity.setId(1L);
        entity.setName("Org 1");
        entity.setContactInfo("c@x.com");

        // Arrange (expected response)
        OrganizerResponse response = new OrganizerResponse();
        response.setId(1L);
        response.setName("Org 1");
        response.setContactInfo("c@x.com");

        // Stubbing
        when(organizerRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(organizerMapper.toResponse(entity)).thenReturn(response);

        // Act
        OrganizerResponse result = organizerService.getById(1L);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        verify(organizerRepository).findById(1L);
        verify(organizerMapper).toResponse(entity);
        verifyNoMoreInteractions(organizerRepository, organizerMapper);
    }

    @Test
    @DisplayName("Get organizer by ID - missing -> throws ResourceNotFoundException")
    void getById_whenMissing_shouldThrow() {
        // Stubbing
        when(organizerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> organizerService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        // Verify (repository called once; mapper should not be called at all)
        verify(organizerRepository).findById(99L);
        verifyNoMoreInteractions(organizerRepository);
        verifyNoInteractions(organizerMapper);
    }

    @Test
    @DisplayName("Get all organizers - happy flow")
    void getAll_shouldReturnList() {
        // Arrange (entities)
        Organizer o1 = new Organizer(); o1.setId(1L); o1.setName("A"); o1.setContactInfo("a@x.com");
        Organizer o2 = new Organizer(); o2.setId(2L); o2.setName("B"); o2.setContactInfo("b@x.com");

        // Arrange (expected responses)
        OrganizerResponse r1 = new OrganizerResponse(); r1.setId(1L); r1.setName("A"); r1.setContactInfo("a@x.com");
        OrganizerResponse r2 = new OrganizerResponse(); r2.setId(2L); r2.setName("B"); r2.setContactInfo("b@x.com");

        // Stubbing
        when(organizerRepository.findAll()).thenReturn(List.of(o1, o2));
        when(organizerMapper.toResponse(o1)).thenReturn(r1);
        when(organizerMapper.toResponse(o2)).thenReturn(r2);

        // Act
        List<OrganizerResponse> result = organizerService.getAll();

        // Assert & Verify
        assertThat(result).hasSize(2);
        verify(organizerRepository).findAll();
        verify(organizerMapper).toResponse(o1);
        verify(organizerMapper).toResponse(o2);
        verifyNoMoreInteractions(organizerRepository, organizerMapper);
    }

    @Test
    @DisplayName("Update organizer - happy flow")
    void updateOrganizerTest() {
        Long id = 1L;

        // Arrange (existing entity in DB)
        Organizer existing = new Organizer();
        existing.setId(id);
        existing.setName("Old Name");
        existing.setContactInfo("old@x.com");

        // Arrange (update request)
        OrganizerRequest request = new OrganizerRequest();
        request.setName("New Name");
        request.setContactInfo("new@x.com");

        // Stubbing
        when(organizerRepository.findById(id)).thenReturn(Optional.of(existing));

        // Stubbing (simulate the mapper updating the entity)
        doAnswer(inv -> {
            Organizer e = inv.getArgument(0);
            OrganizerRequest r = inv.getArgument(1);
            e.setName(r.getName());
            e.setContactInfo(r.getContactInfo());
            return null;
        }).when(organizerMapper).updateEntity(existing, request);

        // Stubbing (simulate saving the updated entity)
        when(organizerRepository.save(existing)).thenReturn(existing);

        // Stubbing (mapping updated entity to response)
        OrganizerResponse response = new OrganizerResponse();
        response.setId(id);
        response.setName("New Name");
        response.setContactInfo("new@x.com");
        when(organizerMapper.toResponse(existing)).thenReturn(response);

        // Act
        OrganizerResponse result = organizerService.update(id, request);

        // Assert
        assertEquals("New Name", result.getName());
        assertEquals("new@x.com", result.getContactInfo());
    }

    @Test
    @DisplayName("Update organizer - missing -> throws ResourceNotFoundException")
    void updateOrganizer_whenMissing_shouldThrow() {
        Long id = 123L;

        // Arrange
        OrganizerRequest request = new OrganizerRequest();
        request.setName("X");
        request.setContactInfo("x@x.com");

        // Stubbing
        when(organizerRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> organizerService.update(id, request)
        );

        assertTrue(ex.getMessage().contains("Organizer with id"));
    }

    @Test
    @DisplayName("Delete organizer - happy flow")
    void delete_whenExists_shouldDelete() {
        when(organizerRepository.existsById(5L)).thenReturn(true);

        organizerService.delete(5L);

        verify(organizerRepository).existsById(5L);
        verify(organizerRepository).deleteById(5L);
        verifyNoMoreInteractions(organizerRepository);
        verifyNoInteractions(organizerMapper);
    }

    @Test
    @DisplayName("Delete organizer - missing -> throws ResourceNotFoundException")
    void delete_whenMissing_shouldThrow() {
        when(organizerRepository.existsById(5L)).thenReturn(false);

        assertThatThrownBy(() -> organizerService.delete(5L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(organizerRepository).existsById(5L);
        verifyNoMoreInteractions(organizerRepository);
        verifyNoInteractions(organizerMapper);
    }
}