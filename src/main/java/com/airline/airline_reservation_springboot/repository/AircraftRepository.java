package com.airline.airline_reservation_springboot.repository;

import com.airline.airline_reservation_springboot.model.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Import Optional

public interface AircraftRepository extends JpaRepository<Aircraft, Integer> {
    // Optional: Add custom find methods if needed, e.g., by model name
    Optional<Aircraft> findByAircraftModelIgnoreCase(String modelName);
}
