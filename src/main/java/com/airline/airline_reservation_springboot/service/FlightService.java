package com.airline.airline_reservation_springboot.service;

import com.airline.airline_reservation_springboot.dto.FlightDetailsDTO;
import com.airline.airline_reservation_springboot.dto.FlightManifestDTO;
import com.airline.airline_reservation_springboot.dto.FlightSummaryDTO;
import com.airline.airline_reservation_springboot.dto.PassengerInfoDTO;
import com.airline.airline_reservation_springboot.dto.SeatLayoutDTO;
import com.airline.airline_reservation_springboot.model.Aircraft;
import com.airline.airline_reservation_springboot.model.Booking;
import com.airline.airline_reservation_springboot.model.Flight;
// import com.airline.airline_reservation_springboot.model.Passenger;
import com.airline.airline_reservation_springboot.repository.BookingRepository;
import com.airline.airline_reservation_springboot.repository.FlightRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional; // <-- This import is required
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final ObjectMapper objectMapper; // Inject Jackson's ObjectMapper

    // Add Logger
    private static final Logger log = LoggerFactory.getLogger(FlightService.class);

    // Update constructor
    public FlightService(FlightRepository flightRepository, BookingRepository bookingRepository, ObjectMapper objectMapper) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.objectMapper = objectMapper; // Assign ObjectMapper
    }

    @Transactional(readOnly = true)
    public List<FlightSummaryDTO> searchFlights(String source, String destination, LocalDate departureDate) {
        // Repository method already filters by status internally via query
        return flightRepository.findBySourceAndDestinationAndDepartureDate(source, destination, departureDate)
                .stream()
                .map(FlightSummaryDTO::new)
                .collect(Collectors.toList());
        /*
         * If repository query didn't filter, you would do this:
         * .stream()
         * .filter(flight -> !"Cancelled".equalsIgnoreCase(flight.getStatus()))
         * .collect(Collectors.toList());
         */
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

    // --- Update getFlightDetails to include parsed layout ---
    @Transactional(readOnly = true)
    public FlightDetailsDTO getFlightDetails(int flightId) {
        Optional<Flight> flightOpt = flightRepository.findById(flightId)
                .filter(flight -> !"Cancelled".equalsIgnoreCase(flight.getStatus()));

        if (flightOpt.isPresent()) {
            Flight flight = flightOpt.get();
            Hibernate.initialize(flight.getAirline());
            Hibernate.initialize(flight.getAircraft()); // Ensure Aircraft is loaded
            Hibernate.initialize(flight.getBookings());

            List<String> bookedSeats = flight.getBookings().stream()
                    .filter(booking -> "CONFIRMED".equalsIgnoreCase(booking.getStatus())
                            || "CHECKED-IN".equalsIgnoreCase(booking.getStatus()))
                    .map(Booking::getSeatNo)
                    .collect(Collectors.toList());

            // --- USE PARSED LAYOUT ---
            // Pass the parsed layout DTO instead of the raw JSON string
            // Optional<SeatLayoutDTO> layoutOpt = parseSeatLayout(flight.getAircraft());

            // Update FlightDetailsDTO constructor/setters if necessary to accept
            // SeatLayoutDTO
            // For now, let's assume FlightDetailsDTO still takes the JSON string,
            // but the React app will parse it. Alternatively, modify FlightDetailsDTO.
            return new FlightDetailsDTO(flight, bookedSeats); // Pass raw JSON for now
            // OR if FlightDetailsDTO accepts SeatLayoutDTO:
            // return new FlightDetailsDTO(flight, bookedSeats, layoutOpt.orElse(null));

        }
        return null;
    }

    // --- Update determineSeatType in BookingService to use this logic ---
    // (This logic might be better placed in FlightService or a dedicated
    // LayoutService)
    @Transactional(readOnly = true) // Needs session to get Aircraft
    public String getSeatTypeNameFromLayout(int flightId, String seatNumber) {
        Optional<Flight> flightOpt = flightRepository.findById(flightId);
        if (flightOpt.isPresent()) {
            Hibernate.initialize(flightOpt.get().getAircraft()); // Load aircraft
            Optional<SeatLayoutDTO> layoutOpt = parseSeatLayout(flightOpt.get().getAircraft());
            if (layoutOpt.isPresent()) {
                // Use helper in SeatLayoutDTO to get type, defaulting to Economy
                return layoutOpt.get().getSeatTypeForSeat(seatNumber);
            }
        }
        log.warn("Could not determine seat type for flight {} seat {}. Defaulting to Economy.", flightId, seatNumber);
        return "Economy"; // Default fallback
    }

    public List<Flight> findAllFlights() {
        return flightRepository.findAll();
    }

    @Transactional
    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }


    @Transactional(readOnly = true)
    public Optional<FlightManifestDTO> getFlightManifestDetails(Integer flightId, String statusFilter) { // Added
                                                                                                         // statusFilter
                                                                                                         // param
        Optional<Flight> flightOpt = flightRepository.findById(flightId);

        if (flightOpt.isPresent()) {
            Flight flight = flightOpt.get();
            Hibernate.initialize(flight.getBookings());
            Hibernate.initialize(flight.getAirline());
            Hibernate.initialize(flight.getAircraft());

            // Start stream
            Stream<Booking> bookingStream = flight.getBookings().stream();

            // --- APPLY FILTER ---
            // Use StringUtils.hasText for safe check (null or empty/whitespace)
            if (StringUtils.hasText(statusFilter)) {
                log.debug("Applying status filter: {}", statusFilter);
                // Filter the stream based on the provided status (case-insensitive)
                bookingStream = bookingStream.filter(booking -> statusFilter.equalsIgnoreCase(booking.getStatus()));
            } else {
                log.debug("No status filter applied, showing all bookings for flight {}", flightId);
            }
            List<PassengerInfoDTO> passengers = bookingStream
                    .map(booking -> {
                        Hibernate.initialize(booking.getUser()); // Initialize user for each booking
                        Hibernate.initialize(booking.getSeatType()); // Initialize seat type
                        return new PassengerInfoDTO( // Assumes PassengerInfoDTO includes booking ID and status now
                                booking.getPassenger().getFullName(),
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

    // --- Helper method to parse JSON ---
    private Optional<SeatLayoutDTO> parseSeatLayout(Aircraft aircraft) {
        if (aircraft == null || aircraft.getSeatLayoutJson() == null || aircraft.getSeatLayoutJson().isBlank()) {
            log.warn("Aircraft {} has no seat layout JSON defined.", aircraft != null ? aircraft.getAircraftModel() : "N/A");
            return Optional.empty();
        }
        try {
            // 1. Parse the entire JSON string into a generic JsonNode tree
            JsonNode rootNode = objectMapper.readTree(aircraft.getSeatLayoutJson());
            // 2. Get the specific "seatmap" node from the tree
            JsonNode seatmapNode = rootNode.path("seatmap"); // Use path to avoid null pointer if key missing

            if (seatmapNode.isMissingNode() || !seatmapNode.isObject()) {
                 log.error("Seat layout JSON for aircraft {} is missing the 'seatmap' object.", aircraft.getAircraftModel());
                 return Optional.empty();
            }

            // 3. Convert the "seatmap" node into our specific SeatLayoutDTO object
            SeatLayoutDTO layout = objectMapper.treeToValue(seatmapNode, SeatLayoutDTO.class);
            return Optional.of(layout);

        } catch (JsonProcessingException e) {
            log.error("Failed to parse seat layout JSON for aircraft {}: {}", aircraft.getAircraftModel(), e.getMessage());
            return Optional.empty();
        }
    }

    // --- STAFF SEARCH WITH AIRLINE FILTER ---
    @Transactional(readOnly = true)
    public List<FlightSummaryDTO> searchFlightsForStaff(
            Integer airlineId, // <-- Added parameter (Use Integer for null check)
            String source,
            String destination,
            String status,
            LocalDate startDate,
            LocalDate endDate) {
        log.debug("Staff search: airlineId={}, src={}, dest={}, status={}, start={}, end={}", airlineId, source, destination, status, startDate, endDate);

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        // Pass airlineId to the repository method
        List<Flight> flights = flightRepository.findFlightsByCriteria(
                airlineId, source, destination, status, startDateTime, endDateTime
        );

        return flights.stream()
                .map(flight -> {
                    Hibernate.initialize(flight.getAirline());
                    Hibernate.initialize(flight.getAircraft());
                    return new FlightSummaryDTO(flight);
                })
                .collect(Collectors.toList());
    }
}

