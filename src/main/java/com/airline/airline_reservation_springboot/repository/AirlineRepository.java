package com.airline.airline_reservation_springboot.repository;

import com.airline.airline_reservation_springboot.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Import Optional

public interface AirlineRepository extends JpaRepository<Airline, Integer> {
    // Optional: Add custom find methods if needed, e.g., by IATA code
    Optional<Airline> findByIataCodeIgnoreCase(String iataCode);

    Optional<Airline> findByAirlineNameIgnoreCase(String name);
}
