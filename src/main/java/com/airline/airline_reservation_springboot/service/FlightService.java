package com.airline.airline_reservation_springboot.service;

import com.airline.airline_reservation_springboot.dto.FlightDetailsDTO;
import com.airline.airline_reservation_springboot.model.Booking;
import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional; // <-- This import is required
import java.util.stream.Collectors;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> searchFlights(String source, String destination, LocalDate departureDate) {
        return flightRepository.findBySourceAndDestinationAndDepartureDate(source, destination, departureDate);
    }

    public Optional<Flight> findById(int flightId) {
        return flightRepository.findById(flightId);
    }

    // Add this method inside your FlightService class

    public FlightDetailsDTO getFlightDetails(int flightId) {
        // Find the flight by its ID
        Optional<Flight> flightOpt = flightRepository.findById(flightId);

        if (flightOpt.isPresent()) {
            Flight flight = flightOpt.get();

            // Get the list of all bookings for this flight
            List<Booking> bookings = flight.getBookings();

            // Extract just the seat numbers from the list of bookings
            List<String> bookedSeats = bookings.stream()
                                            .map(Booking::getSeat)
                                            .collect(Collectors.toList());

            // Create and return the new DTO
            return new FlightDetailsDTO(flight, bookedSeats);
        }

        // Return null if the flight was not found
        return null;
    }
}

