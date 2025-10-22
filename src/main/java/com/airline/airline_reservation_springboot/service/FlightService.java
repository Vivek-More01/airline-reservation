package com.airline.airline_reservation_springboot.service;

import com.airline.airline_reservation_springboot.dto.FlightDetailsDTO;
import com.airline.airline_reservation_springboot.dto.FlightManifestDTO;
import com.airline.airline_reservation_springboot.dto.FlightSummaryDTO;
import com.airline.airline_reservation_springboot.dto.PassengerInfoDTO;
import com.airline.airline_reservation_springboot.model.Booking;
import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.repository.FlightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

            List<String> bookedSeats = bookings.stream()
                    .filter(booking -> "CONFIRMED".equalsIgnoreCase(booking.getStatus())) // Only consider active bookings
                    .map(Booking::getSeat)
                    .collect(Collectors.toList());

            // Create and return the new DTO
            return new FlightDetailsDTO(flight, bookedSeats);
        }

        // Return null if the flight was not found
        return null;
    }

    public List<Flight> findAllFlights() {
        return flightRepository.findAll();
    }

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    @Transactional(readOnly = true)
    public Optional<FlightManifestDTO> getFlightManifestDetails(Integer flightId) {
        Optional<Flight> flightOpt = flightRepository.findById(flightId);

        if (flightOpt.isPresent()) {
            Flight flight = flightOpt.get();
            List<PassengerInfoDTO> passengers = flight.getBookings().stream()
                .map(booking -> new PassengerInfoDTO(
                    booking.getUser().getName(),
                    booking.getUser().getEmail(),
                    booking.getSeat(),
                    booking.getPnr()
                ))
                .collect(Collectors.toList());

            FlightManifestDTO manifestDTO = new FlightManifestDTO(
                flight.getAirline(),
                flight.getSource(),
                flight.getDestination(),
                passengers
            );
            return Optional.of(manifestDTO);
        }
        return Optional.empty();
    }

    /**
     * Fetches all flights and converts them into a list of summaries (DTOs).
     * This avoids potential lazy loading issues in the view.
     */
    public List<FlightSummaryDTO> getAllFlightSummaries() {
        // System.out.println("Fetching all flight summaries from FlightService."+ flightRepository.findAll().stream()
           // .collect(Collectors.toList()));
        return flightRepository.findAll().stream()
            .map(flight -> new FlightSummaryDTO(
                flight.getFlightId(),
                flight.getAirline(),
                flight.getSource(),
                flight.getDestination(),
                flight.getDeparture(),
                flight.getSeatsAvailable(),
                flight.getSeatsTotal()
            ))
            .collect(Collectors.toList());
    }
}

