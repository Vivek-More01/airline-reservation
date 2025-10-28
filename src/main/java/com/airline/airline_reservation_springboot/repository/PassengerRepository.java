package com.airline.airline_reservation_springboot.repository;

import com.airline.airline_reservation_springboot.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
    // Add custom find methods if needed later, e.g., by passport number
}
