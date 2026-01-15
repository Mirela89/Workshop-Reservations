package com.example.workshop_reservations.service;

import com.example.workshop_reservations.dto.WorkshopRequest;
import com.example.workshop_reservations.dto.WorkshopResponse;
import com.example.workshop_reservations.exception.ResourceNotFoundException;
import com.example.workshop_reservations.mapper.WorkshopMapper;
import com.example.workshop_reservations.model.*;
import com.example.workshop_reservations.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkshopServiceTest {

    @Mock private WorkshopRepository workshopRepository;
    @Mock private WorkshopMapper workshopMapper;

    @Mock private ReservationRepository reservationRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private LocationRepository locationRepository;
    @Mock private OrganizerRepository organizerRepository;

    @InjectMocks private WorkshopService workshopService;

    @Test
    void create_whenAllRelatedEntitiesExist_savesAndReturnsResponse() {
        // Arrange
        WorkshopRequest req = mock(WorkshopRequest.class);
        when(req.getCategoryId()).thenReturn(1L);
        when(req.getLocationId()).thenReturn(2L);
        when(req.getOrganizerId()).thenReturn(3L);

        Category category = new Category();
        category.setId(1L);
        Location location = new Location();
        location.setId(2L);
        Organizer organizer = new Organizer();
        organizer.setId(3L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(locationRepository.findById(2L)).thenReturn(Optional.of(location));
        when(organizerRepository.findById(3L)).thenReturn(Optional.of(organizer));

        Workshop entity = new Workshop();
        when(workshopMapper.toEntity(req)).thenReturn(entity);

        Workshop saved = new Workshop();
        saved.setId(10L);
        when(workshopRepository.save(any(Workshop.class))).thenReturn(saved);

        WorkshopResponse response = new WorkshopResponse();
        response.setId(10L);
        when(workshopMapper.toResponse(saved)).thenReturn(response);

        // Act
        WorkshopResponse result = workshopService.create(req);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());

        // Verify relationships set before save
        ArgumentCaptor<Workshop> captor = ArgumentCaptor.forClass(Workshop.class);
        verify(workshopRepository).save(captor.capture());
        Workshop toSave = captor.getValue();
        assertSame(category, toSave.getCategory());
        assertSame(location, toSave.getLocation());
        assertSame(organizer, toSave.getOrganizer());
    }

    @Test
    void create_whenCategoryMissing_throwsNotFound() {
        WorkshopRequest req = mock(WorkshopRequest.class);
        when(req.getCategoryId()).thenReturn(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workshopService.create(req));

        verify(workshopRepository, never()).save(any());
        verify(locationRepository, never()).findById(anyLong());
        verify(organizerRepository, never()).findById(anyLong());
        verify(workshopMapper, never()).toEntity(any());
    }

    @Test
    void create_whenLocationMissing_throwsNotFound() {
        WorkshopRequest req = mock(WorkshopRequest.class);
        when(req.getCategoryId()).thenReturn(1L);
        when(req.getLocationId()).thenReturn(2L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category()));
        when(locationRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workshopService.create(req));

        verify(organizerRepository, never()).findById(anyLong());
        verify(workshopMapper, never()).toEntity(any());
        verify(workshopRepository, never()).save(any());
    }

    @Test
    void create_whenOrganizerMissing_throwsNotFound() {
        WorkshopRequest req = mock(WorkshopRequest.class);
        when(req.getCategoryId()).thenReturn(1L);
        when(req.getLocationId()).thenReturn(2L);
        when(req.getOrganizerId()).thenReturn(3L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category()));
        when(locationRepository.findById(2L)).thenReturn(Optional.of(new Location()));
        when(organizerRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workshopService.create(req));
        verify(workshopRepository, never()).save(any());
    }

    @Test
    void getAll_shouldReturnResponsesWithReservedAndAvailableSeats() {
        // Arrange
        Workshop w1 = new Workshop();
        w1.setId(1L);
        w1.setCapacity(20);

        Workshop w2 = new Workshop();
        w2.setId(2L);
        w2.setCapacity(10);

        when(workshopRepository.findAll()).thenReturn(List.of(w1, w2));

        when(reservationRepository.sumSeatsByWorkshopAndStatus(1L, ReservationStatus.CREATED)).thenReturn(5);
        when(reservationRepository.sumSeatsByWorkshopAndStatus(2L, ReservationStatus.CREATED)).thenReturn(0);

        WorkshopResponse r1 = new WorkshopResponse();
        r1.setId(1L);
        WorkshopResponse r2 = new WorkshopResponse();
        r2.setId(2L);

        when(workshopMapper.toResponse(w1)).thenReturn(r1);
        when(workshopMapper.toResponse(w2)).thenReturn(r2);

        // Act
        List<WorkshopResponse> result = workshopService.getAll();

        // Assert
        assertEquals(2, result.size());

        WorkshopResponse out1 = result.get(0);
        assertEquals(1L, out1.getId());
        assertEquals(5, out1.getReservedSeats());
        assertEquals(15, out1.getAvailableSeats());

        WorkshopResponse out2 = result.get(1);
        assertEquals(2L, out2.getId());
        assertEquals(0, out2.getReservedSeats());
        assertEquals(10, out2.getAvailableSeats());
    }

    @Test
    void getById_whenExists_shouldComputeSeats() {
        // Arrange
        Workshop w = new Workshop();
        w.setId(7L);
        w.setCapacity(30);

        when(workshopRepository.findById(7L)).thenReturn(Optional.of(w));
        when(reservationRepository.sumSeatsByWorkshopAndStatus(7L, ReservationStatus.CREATED)).thenReturn(12);

        WorkshopResponse resp = new WorkshopResponse();
        resp.setId(7L);
        when(workshopMapper.toResponse(w)).thenReturn(resp);

        // Act
        WorkshopResponse result = workshopService.getById(7L);

        // Assert
        assertNotNull(result);
        assertEquals(7L, result.getId());
        assertEquals(12, result.getReservedSeats());
        assertEquals(18, result.getAvailableSeats());
    }

    @Test
    void getById_whenMissing_shouldThrowNotFound() {
        when(workshopRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workshopService.getById(99L));

        verify(reservationRepository, never()).sumSeatsByWorkshopAndStatus(anyLong(), any());
    }

    @Test
    void update_whenExists_updatesEntityAndReturnsResponse() {
        // Arrange
        Long id = 5L;
        Workshop existing = new Workshop();
        existing.setId(id);

        WorkshopRequest req = mock(WorkshopRequest.class);
        when(req.getCategoryId()).thenReturn(1L);
        when(req.getLocationId()).thenReturn(2L);
        when(req.getOrganizerId()).thenReturn(3L);

        when(workshopRepository.findById(id)).thenReturn(Optional.of(existing));

        Category category = new Category();
        Location location = new Location();
        Organizer organizer = new Organizer();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(locationRepository.findById(2L)).thenReturn(Optional.of(location));
        when(organizerRepository.findById(3L)).thenReturn(Optional.of(organizer));

        // Mapper updateEntity should be invoked
        doNothing().when(workshopMapper).updateEntity(existing, req);

        Workshop saved = new Workshop();
        saved.setId(id);
        when(workshopRepository.save(existing)).thenReturn(saved);

        WorkshopResponse resp = new WorkshopResponse();
        resp.setId(id);
        when(workshopMapper.toResponse(saved)).thenReturn(resp);

        // Act
        WorkshopResponse result = workshopService.update(id, req);

        // Assert
        assertEquals(id, result.getId());
        verify(workshopMapper).updateEntity(existing, req);

        // Verify relationships set before save
        assertSame(category, existing.getCategory());
        assertSame(location, existing.getLocation());
        assertSame(organizer, existing.getOrganizer());
    }

    @Test
    void update_whenWorkshopMissing_shouldThrowNotFound() {
        when(workshopRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workshopService.update(123L, mock(WorkshopRequest.class)));

        verify(workshopRepository, never()).save(any());
    }

    @Test
    void cancelWorkshop_whenActive_shouldSetCanceledAndCancelReservations() {
        Long id = 11L;
        Workshop w = new Workshop();
        w.setId(id);
        w.setStatus(WorkshopStatus.ACTIVE);

        when(workshopRepository.findById(id)).thenReturn(Optional.of(w));
        when(workshopRepository.save(any(Workshop.class))).thenAnswer(inv -> inv.getArgument(0));

        workshopService.cancelWorkshop(id);

        assertEquals(WorkshopStatus.CANCELED, w.getStatus());
        verify(workshopRepository).save(w);
        verify(reservationRepository).cancelByWorkshopId(id);
    }

    @Test
    void cancelWorkshop_whenAlreadyCanceled_shouldDoNothing() {
        Long id = 12L;
        Workshop w = new Workshop();
        w.setId(id);
        w.setStatus(WorkshopStatus.CANCELED);

        when(workshopRepository.findById(id)).thenReturn(Optional.of(w));

        workshopService.cancelWorkshop(id);

        verify(workshopRepository, never()).save(any());
        verify(reservationRepository, never()).cancelByWorkshopId(anyLong());
    }

    @Test
    void cancelWorkshop_whenMissing_shouldThrowNotFound() {
        when(workshopRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> workshopService.cancelWorkshop(404L));

        verify(reservationRepository, never()).cancelByWorkshopId(anyLong());
    }

    @Test
    void delete_whenExists_deletes() {
        when(workshopRepository.existsById(1L)).thenReturn(true);

        workshopService.delete(1L);

        verify(workshopRepository).deleteById(1L);
    }

    @Test
    void delete_whenMissing_shouldThrowNotFound() {
        when(workshopRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> workshopService.delete(2L));

        verify(workshopRepository, never()).deleteById(anyLong());
    }
}