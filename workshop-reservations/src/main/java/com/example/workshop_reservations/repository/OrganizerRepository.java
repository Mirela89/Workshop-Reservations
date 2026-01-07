package com.example.workshop_reservations.repository;

import com.example.workshop_reservations.model.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizerRepository extends JpaRepository<Organizer, Long> {
    boolean existsByNameIgnoreCase(String name);
}