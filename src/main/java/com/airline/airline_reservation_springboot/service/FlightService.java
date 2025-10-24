package com.airline.airline_reservation_springboot.service;

import com.airline.airline_reservation_springboot.dto.FlightDetailsDTO;
import com.airline.airline_reservation_springboot.dto.FlightManifestDTO;
import com.airline.airline_reservation_springboot.dto.FlightSummaryDTO;
import com.airline.airline_reservation_springboot.dto.PassengerInfoDTO;
import com.airline.airline_reservation_springboot.model.Booking;
import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.repository.BookingRepository;
import com.airline.airline_reservation_springboot.repository.FlightRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional; // <-- This import is required
import java.util.stream.Collectors;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    public FlightService(FlightRepository flightRepository, BookingRepository bookingRepository) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Flight> searchFlights(String source, String destination, LocalDate departureDate) {
        return flightRepository.findBySourceAndDestinationAndDepartureDate(source, destination, departureDate).stream()
                .filter(flight -> !"Cancelled".equalsIgnoreCase(flight.getStatus()))
                .collect(Collectors.toList());
    }

    public Optional<Flight> findById(int flightId) {
        return flightRepository.findById(flightId);
    }

    // Add this method inside your FlightService class

    public FlightDetailsDTO getFlightDetails(int flightId) {
        // Find the flight by its ID
        Optional<Flight> flightOpt = flightRepository.findById(flightId)
                .filter(flight -> !"Cancelled".equalsIgnoreCase(flight.getStatus()));;

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
                    booking.getPnr(),
                    booking.getBookingId(),
                    booking.getStatus()
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
                flight.getSeatsTotal(),
                flight.getStatus()
            ))
            .collect(Collectors.toList());
    }

    // --- METHODS FOR FLIGHT STATUS MANAGEMENT ---

    /**
     * Cancels a flight and updates associated bookings.
     * @param flightId The ID of the flight to cancel.
     * @return true if successful, false if flight not found.
     */
    @Transactional
    public boolean cancelFlight(Integer flightId) {
        Optional<Flight> flightOpt = flightRepository.findById(flightId);
        if (flightOpt.isPresent()) {
            Flight flight = flightOpt.get();
            if ("Cancelled".equalsIgnoreCase(flight.getStatus())) {
                return true; // Already cancelled
            }
            flight.setStatus("Cancelled");
            flight.setSeatsAvailable(0); // No more seats available
            flightRepository.save(flight);

            // Cancel all associated confirmed bookings
            List<Booking> bookingsToCancel = flight.getBookings().stream()
                    .filter(b -> "CONFIRMED".equalsIgnoreCase(b.getStatus()))
                    .collect(Collectors.toList());

            for (Booking booking : bookingsToCancel) {
                booking.setStatus("CANCELLED");
            }
            bookingRepository.saveAll(bookingsToCancel);
            // In a real system, you would trigger notifications here

            return true;
        }
        return false; // Flight not found
    }

     /**
     * Delays a flight by updating its departure and arrival times.
     * @param flightId The ID of the flight to delay.
     * @param newDeparture The new departure time.
     * @param newArrival The new arrival time.
     * @return true if successful, false if flight not found or already cancelled.
     */
    @Transactional
    public boolean delayFlight(Integer flightId, LocalDateTime newDeparture, LocalDateTime newArrival) {
        Optional<Flight> flightOpt = flightRepository.findById(flightId);
        if (flightOpt.isPresent()) {
            Flight flight = flightOpt.get();
             if ("Cancelled".equalsIgnoreCase(flight.getStatus())) {
                return false; // Cannot delay a cancelled flight
            }
            flight.setDeparture(newDeparture);
            flight.setArrival(newArrival);
            flight.setStatus("Delayed");
            flightRepository.save(flight);
            // In a real system, you would trigger notifications here
            return true;
        }
        return false; // Flight not found
    }
}

