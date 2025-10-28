package com.airline.airline_reservation_springboot.repository;

import com.airline.airline_reservation_springboot.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {

    // Query for searching flights (checks status)
    @Query("SELECT f FROM Flight f WHERE LOWER(f.source) = LOWER(:source) AND LOWER(f.destination) = LOWER(:destination) AND CAST(f.departure AS date) = :departureDate AND f.status <> 'Cancelled'")
    List<Flight> findBySourceAndDestinationAndDepartureDate(@Param("source") String source,
            @Param("destination") String destination, @Param("departureDate") LocalDate departureDate);

    // Counts active flights (not cancelled)
    long countByStatusNotIgnoreCase(String status); // Status = "Cancelled"

    // Calculates average load factor (uses derived total seats indirectly via
    // Flight entity)
    // Note: If performance becomes an issue, join with Aircraft table directly
    @Query("SELECT AVG( (CAST(f.aircraft.totalSeats AS DOUBLE) - CAST(f.seatsAvailable AS DOUBLE)) / CAST(f.aircraft.totalSeats AS DOUBLE) ) "
            +
            "FROM Flight f WHERE f.status <> 'Cancelled' AND f.aircraft.totalSeats > 0")
    Double calculateAverageLoadFactor();

    // Optional: Find flights by airline
    // List<Flight> findByAirline(Airline airline);

    @Query("SELECT f FROM Flight f WHERE " +
    // *** ADDED Airline Filter ***
                    "(:airlineId IS NULL OR f.airline.airlineId = :airlineId) AND " +
                    "(:source IS NULL OR :source = '' OR LOWER(f.source) LIKE LOWER(CONCAT('%', :source, '%'))) AND " +
                    "(:destination IS NULL OR :destination = '' OR LOWER(f.destination) LIKE LOWER(CONCAT('%', :destination, '%'))) AND "
                    +
                    "(:status IS NULL OR :status = '' OR LOWER(f.status) = LOWER(:status)) AND " +
                    "(CAST(:startDate AS timestamp) IS NULL OR f.departure >= :startDate) AND " +
                    "(CAST(:endDate AS timestamp) IS NULL OR f.departure <= :endDate) " +
                    "ORDER BY f.departure ASC")
    List<Flight> findFlightsByCriteria(
                    @Param("airlineId") Integer airlineId, // <-- Added parameter (Use Integer for null check)
                    @Param("source") String source,
                    @Param("destination") String destination,
                    @Param("status") String status,
                    @Param("startDate") LocalDateTime startDate,
                    @Param("endDate") LocalDateTime endDate);
}
