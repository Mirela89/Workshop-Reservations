package com.example.workshop_reservations.repository;

import com.example.workshop_reservations.model.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {
}