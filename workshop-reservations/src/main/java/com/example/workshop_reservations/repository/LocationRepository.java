package com.example.workshop_reservations.repository;

import com.example.workshop_reservations.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}