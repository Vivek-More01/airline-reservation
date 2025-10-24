package com.airline.airline_reservation_springboot.repository;

import com.airline.airline_reservation_springboot.model.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Import Optional

public interface SeatTypeRepository extends JpaRepository<SeatType, Integer> {
    // Find by the unique type name (useful for getting the ID)
    Optional<SeatType> findByTypeNameIgnoreCase(String typeName);
}
