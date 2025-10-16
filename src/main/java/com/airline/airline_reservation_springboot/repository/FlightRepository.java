package com.airline.airline_reservation_springboot.repository;

import com.airline.airline_reservation_springboot.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {

    // This custom query explicitly casts the 'departure' DATETIME field to a DATE
    // to correctly compare it with the incoming LocalDate parameter.
    @Query("SELECT f FROM Flight f WHERE f.source = :source AND f.destination = :destination AND CAST(f.departure AS DATE) = :departureDate")
    List<Flight> findBySourceAndDestinationAndDepartureDate(
            @Param("source") String source,
            @Param("destination") String destination,
            @Param("departureDate") LocalDate departureDate);
}
