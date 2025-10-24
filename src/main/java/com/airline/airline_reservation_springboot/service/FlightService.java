package com.airline.airline_reservation_springboot.service;

import com.airline.airline_reservation_springboot.dto.FlightDetailsDTO;
import com.airline.airline_reservation_springboot.dto.FlightManifestDTO;
import com.airline.airline_reservation_springboot.dto.FlightSummaryDTO;
import com.airline.airline_reservation_springboot.dto.PassengerInfoDTO;
import com.airline.airline_reservation_springboot.model.Booking;
import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.repository.BookingRepository;
import com.airline.airline_reservation_springboot.repository.FlightRepository;

import org.hibernate.Hibernate;
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

    @Transactional(readOnly = true) // Needed for accessing related Airline/Aircraft for DTO
    public List<FlightSummaryDTO> searchFlights(String source, String destination,
            LocalDate departureDate) {
        // Fetch the flight entities matching the criteria (already filters cancelled)
        List<Flight> flights = flightRepository.findBySourceAndDestinationAndDepartureDate(source, destination,
                departureDate);

        // Map each Flight entity to a FlightSummaryDTO
        return flights.stream()
                .map(flight -> {
                    // Eagerly initialize necessary fields within the transaction
                    Hibernate.initialize(flight.getAirline());
                    Hibernate.initialize(flight.getAircraft());
                    // Create and return the Summary DTO
                    return new FlightSummaryDTO(flight); // Use DTO constructor
                })
                .collect(Collectors.toList());
    }

    /** Finds a flight by ID. Transactional for potential lazy loading access later. */
     @Transactional(readOnly = true)
    public Optional<Flight> findById(int flightId) {
         // Eagerly fetch related entities needed frequently to avoid lazy issues downstream
         Optional<Flight> flightOpt = flightRepository.findById(flightId);
         flightOpt.ifPresent(flight -> {
             Hibernate.initialize(flight.getAirline());
             Hibernate.initialize(flight.getAircraft());
         });
        return flightOpt;
        // return flightRepository.findById(flightId); // Simpler version if lazy loading isn't an issue yet
    }

    // Add this method inside your FlightService class

    /** Fetches detailed flight info including booked seats for the seat map API. */
    @Transactional(readOnly = true)
    public FlightDetailsDTO getFlightDetails(int flightId) {
        Optional<Flight> flightOpt = flightRepository.findById(flightId)
                .filter(flight -> !"Cancelled".equalsIgnoreCase(flight.getStatus()));

        if (flightOpt.isPresent()) {
            Flight flight = flightOpt.get();
            // Explicitly initialize lazy collections while session is open
            Hibernate.initialize(flight.getBookings());
            Hibernate.initialize(flight.getAirline());
            Hibernate.initialize(flight.getAircraft());

            List<String> bookedSeats = flight.getBookings().stream()
                                               .filter(booking -> "CONFIRMED".equalsIgnoreCase(booking.getStatus()) || "CHECKED-IN".equalsIgnoreCase(booking.getStatus()))
                                               .map(Booking::getSeatNo)
                                               .collect(Collectors.toList());
            // DTO constructor now handles fetching related names/models safely
            return new FlightDetailsDTO(flight, bookedSeats);
        }
        return null;
    }

    public List<Flight> findAllFlights() {
        return flightRepository.findAll();
    }

    @Transactional
    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }


    @Transactional(readOnly = true)
    public Optional<FlightManifestDTO> getFlightManifestDetails(Integer flightId) {
        Optional<Flight> flightOpt = flightRepository.findById(flightId);

        if (flightOpt.isPresent()) {
            Flight flight = flightOpt.get();
            // Eagerly initialize collections needed for the DTO
            Hibernate.initialize(flight.getBookings());
            Hibernate.initialize(flight.getAirline()); // Needed for airline name
            Hibernate.initialize(flight.getAircraft()); // Needed for total seats

            List<PassengerInfoDTO> passengers = flight.getBookings().stream()
                    .filter(booking -> "CONFIRMED".equalsIgnoreCase(booking.getStatus())
                            || "CHECKED-IN".equalsIgnoreCase(booking.getStatus())) // Filter for active passengers
                    .map(booking -> {
                        Hibernate.initialize(booking.getUser()); // Initialize user for each booking
                        Hibernate.initialize(booking.getSeatType()); // Initialize seat type
                        return new PassengerInfoDTO( // Assumes PassengerInfoDTO includes booking ID and status now
                                booking.getUser().getName(),
                                booking.getUser().getEmail(),
                                booking.getSeatNo(),
                                booking.getPnr(),
                                booking.getBookingId(),
                                booking.getStatus());
                    })
                    .collect(Collectors.toList());

            // Build DTO
            FlightManifestDTO manifestDTO = new FlightManifestDTO(
                    flight.getAirline().getAirlineName(),
                    flight.getSource(),
                    flight.getDestination(),
                    passengers,
                    flight.getTotalSeats(), // Use helper method on Flight entity
                    flight.getSeatsAvailable());
            return Optional.of(manifestDTO);
        }
        return Optional.empty();
    }

    /**
     * Fetches all flights and converts them into a list of summaries (DTOs).
     * This avoids potential lazy loading issues in the view.
     */
    @Transactional(readOnly = true) // Needed to access related Airline/Aircraft
    public List<FlightSummaryDTO> getAllFlightSummaries() {
        return flightRepository.findAll().stream()
                .map(flight -> {
                    // Ensure related entities are loaded before mapping
                    Hibernate.initialize(flight.getAirline());
                    Hibernate.initialize(flight.getAircraft());
                    return new FlightSummaryDTO(flight); // Use the DTO's constructor
                })
                .collect(Collectors.toList());
    }

    // --- METHODS FOR FLIGHT STATUS MANAGEMENT ---

    // --- Flight Status Management ---

    @Transactional
    public boolean cancelFlight(Integer flightId) {
        Optional<Flight> flightOpt = flightRepository.findById(flightId);
        if (flightOpt.isPresent()) {
            Flight flight = flightOpt.get();
            if ("Cancelled".equalsIgnoreCase(flight.getStatus()))
                return true;

            flight.setStatus("Cancelled");
            flight.setSeatsAvailable(0);
            flightRepository.save(flight);

            // Fetch bookings separately to modify them
            List<Booking> bookingsToCancel = bookingRepository.findByFlight(flight).stream() // Assuming findByFlight
                                                                                             // exists
                    .filter(b -> "CONFIRMED".equalsIgnoreCase(b.getStatus())
                            || "CHECKED-IN".equalsIgnoreCase(b.getStatus()))
                    .collect(Collectors.toList());

            if (!bookingsToCancel.isEmpty()) {
                for (Booking booking : bookingsToCancel) {
                    booking.setStatus("CANCELLED");
                    // No need to adjust seatsAvailable here as the flight is cancelled
                }
                bookingRepository.saveAll(bookingsToCancel);
            }
            return true;
        }
        return false;
    }

    @Transactional
    public boolean delayFlight(Integer flightId, LocalDateTime newDeparture, LocalDateTime newArrival) {
        Optional<Flight> flightOpt = flightRepository.findById(flightId);
        if (flightOpt.isPresent()) {
            Flight flight = flightOpt.get();
            if ("Cancelled".equalsIgnoreCase(flight.getStatus()))
                return false;

            flight.setDeparture(newDeparture);
            flight.setArrival(newArrival);
            flight.setStatus("Delayed");
            flightRepository.save(flight);
            return true;
        }
        return false;
    }
}

