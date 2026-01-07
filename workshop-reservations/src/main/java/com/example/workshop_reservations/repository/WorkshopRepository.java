package com.example.workshop_reservations.repository;

import com.example.workshop_reservations.model.Workshop;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {

    // Pessimistic lock to prevent overselling seats during concurrent reservations
    // Find workshop by id with a PESSIMISTIC_WRITE lock
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select w from Workshop w where w.id = :id")
    Optional<Workshop> findByIdForUpdate(Long id);
}