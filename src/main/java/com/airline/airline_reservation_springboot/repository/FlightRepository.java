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

    @Query("SELECT f FROM Flight f WHERE LOWER(f.source) = LOWER(:source) AND LOWER(f.destination) = LOWER(:destination) AND CAST(f.departure AS date) = :departureDate AND f.status <> 'Cancelled'")
    List<Flight> findBySourceAndDestinationAndDepartureDate(@Param("source") String source,
            @Param("destination") String destination, @Param("departureDate") LocalDate departureDate);

    // --- NEW METHODS FOR ANALYTICS ---

    /** Counts flights that are not cancelled. */
    long countByStatusNotIgnoreCase(String status);

    /**
     * Calculates the average load factor across all non-cancelled flights with
     * total seats > 0.
     */
    @Query("SELECT AVG( (CAST(f.seatsTotal AS DOUBLE) - CAST(f.seatsAvailable AS DOUBLE)) / CAST(f.seatsTotal AS DOUBLE) ) "
            +
            "FROM Flight f WHERE f.status <> 'Cancelled' AND f.seatsTotal > 0")
    Double calculateAverageLoadFactor();
}
