package com.airline.airline_reservation_springboot.service;

import com.airline.airline_reservation_springboot.dto.BookingConfirmationDTO;
import com.airline.airline_reservation_springboot.dto.BookingSummaryDTO;
import com.airline.airline_reservation_springboot.model.*; // Import all models
import com.airline.airline_reservation_springboot.repository.BookingRepository;
import com.airline.airline_reservation_springboot.repository.FlightRepository;
import com.airline.airline_reservation_springboot.repository.PricingRuleRepository; // Import PricingRuleRepository
import com.airline.airline_reservation_springboot.repository.SeatTypeRepository; // Import SeatTypeRepository
import org.hibernate.Hibernate; // Import Hibernate
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal; // Import BigDecimal
import java.math.RoundingMode; // Import RoundingMode
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher; // Import Matcher
import java.util.regex.Pattern; // Import Pattern
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final SeatTypeRepository seatTypeRepository; // Inject SeatTypeRepository
    private final PricingRuleRepository pricingRuleRepository; // Inject PricingRuleRepository

    // Updated Constructor
    public BookingService(BookingRepository bookingRepository,
            FlightRepository flightRepository,
            SeatTypeRepository seatTypeRepository,
            PricingRuleRepository pricingRuleRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.seatTypeRepository = seatTypeRepository;
        this.pricingRuleRepository = pricingRuleRepository;
    }

    // --- UPDATED createBooking Method ---
    @Transactional
    public Booking createBooking(User user, int flightId, String seatNumber) {
        // 1. Fetch Flight and ensure related entities are loaded
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid flight ID: " + flightId));
        Hibernate.initialize(flight.getAirline());
        Hibernate.initialize(flight.getAircraft()); // Needed for layout/type logic

        // 2. Basic Checks
        if (flight.getSeatsAvailable() <= 0 && !isSeatTaken(flight, seatNumber)) {
            throw new IllegalStateException("No seats available on this flight.");
        }
        if (!"Scheduled".equalsIgnoreCase(flight.getStatus()) && !"Delayed".equalsIgnoreCase(flight.getStatus())) {
            throw new IllegalStateException(
                    "This flight cannot be booked currently (Status: " + flight.getStatus() + ").");
        }
        if (isSeatTaken(flight, seatNumber)) {
            throw new IllegalStateException("Seat " + seatNumber + " is already taken.");
        }

        // 3. Determine Seat Type and Calculate Price
        SeatType seatType = determineSeatType(flight.getAircraft(), seatNumber); // Use helper
        BigDecimal calculatedPrice = calculatePrice(flight, seatType); // Use helper

        // 4. Update Flight Availability
        flight.setSeatsAvailable(flight.getSeatsAvailable() - 1);
        flightRepository.save(flight);

        // 5. Create and Save Booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setSeatNo(seatNumber);
        booking.setSeatType(seatType); // Set the determined SeatType
        booking.setCalculatedPrice(calculatedPrice); // Set the calculated price
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus("CONFIRMED");
        booking.setPnr(generatePnr());

        return bookingRepository.save(booking);
    }

    public Optional<Booking> findById(Integer bookingId) {
        return bookingRepository.findById(bookingId);
    }

    // --- UPDATED DTO Creation Logic ---
    @Transactional(readOnly = true)
    public Optional<BookingConfirmationDTO> getBookingConfirmationDetails(Integer bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            // Explicitly initialize needed entities
            Hibernate.initialize(booking.getUser());
            Hibernate.initialize(booking.getFlight());
            Hibernate.initialize(booking.getFlight().getAirline());
            Hibernate.initialize(booking.getSeatType());
            // Use the DTO constructor that takes the entity
            return Optional.of(new BookingConfirmationDTO(booking));
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public List<BookingSummaryDTO> findBookingsByUser(User user) {
        // Find bookings for the user
        List<Booking> bookings = bookingRepository.findByUserOrderByBookingDateDesc(user);
        // Initialize necessary related entities before mapping
        bookings.forEach(booking -> {
            Hibernate.initialize(booking.getFlight());
            Hibernate.initialize(booking.getFlight().getAirline());
            Hibernate.initialize(booking.getSeatType());
        });
        // Map to DTOs
        return bookings.stream()
                .map(BookingSummaryDTO::new) // Use the DTO constructor
                .collect(Collectors.toList());
    }

    // --- Cancellation Logic (Mostly Unchanged, uses shared helper) ---
    @Transactional
    public boolean cancelBooking(Integer bookingId, User user) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            // Security check: Ensure the booking belongs to the current user
            // Initialize user proxy before accessing ID
            Hibernate.initialize(booking.getUser());
            if (!booking.getUser().getUserId().equals(user.getUserId())) {
                return false;
            }
            if (!"CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
                return false;
            }
            Hibernate.initialize(booking.getFlight()); // Ensure flight is loaded for cancellation logic
            return performCancellation(booking);
        }
        return false;
    }

    @Transactional
    public boolean checkInPassenger(Integer bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            if ("CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
                booking.setStatus("CHECKED-IN");
                bookingRepository.save(booking);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean cancelBookingByStaff(Integer bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
                return true;
            }
            Hibernate.initialize(booking.getFlight()); // Ensure flight is loaded for cancellation logic
            return performCancellation(booking);
        }
        return false;
    }

    private boolean performCancellation(Booking booking) {
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
        Flight flight = booking.getFlight(); // Already initialized if called from transactional method
        if (flight != null && !"Cancelled".equalsIgnoreCase(flight.getStatus())) {
            flight.setSeatsAvailable(flight.getSeatsAvailable() + 1);
            flightRepository.save(flight);
        }
        return true;
    }

    // --- UPDATED changeSeatNumber Method ---
    @Transactional
    public boolean changeSeatNumber(Integer bookingId, String newSeatNumber) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

        if (!"CONFIRMED".equalsIgnoreCase(booking.getStatus()) && !"CHECKED-IN".equalsIgnoreCase(booking.getStatus())) {
            throw new IllegalArgumentException("Cannot change seat for a booking with status: " + booking.getStatus());
        }

        Flight flight = booking.getFlight();
        Hibernate.initialize(flight.getAircraft()); // Need aircraft for seat type logic
        Hibernate.initialize(flight.getBookings()); // Need bookings to check availability

        // Check if the NEW seat is available (ignoring the passenger's CURRENT seat)
        boolean isNewSeatTaken = flight.getBookings().stream()
                .filter(b -> !b.getBookingId().equals(bookingId))
                .filter(b -> "CONFIRMED".equalsIgnoreCase(b.getStatus())
                        || "CHECKED-IN".equalsIgnoreCase(b.getStatus()))
                .anyMatch(b -> newSeatNumber.equalsIgnoreCase(b.getSeatNo()));

        if (isNewSeatTaken) {
            throw new IllegalStateException("Seat " + newSeatNumber + " is already taken on this flight.");
        }

        // --- Determine new SeatType and potentially recalculate price ---
        SeatType newSeatType = determineSeatType(flight.getAircraft(), newSeatNumber);
        // Decide if price should change. For simplicity, we won't change it now.
        // BigDecimal newCalculatedPrice = calculatePrice(flight, newSeatType);
        // booking.setCalculatedPrice(newCalculatedPrice);

        booking.setSeatNo(newSeatNumber);
        booking.setSeatType(newSeatType); // Update the seat type association
        bookingRepository.save(booking);
        return true;
    }

    // --- Helper Methods ---

    /** Determines the SeatType based on aircraft layout and seat number. */
    private SeatType determineSeatType(Aircraft aircraft, String seatNumber) {
        // Basic logic: Use row number. Customize with JSON parsing later if needed.
        Pattern pattern = Pattern.compile("(\\d+)([A-Za-z]+)"); // Regex to extract row number
        Matcher matcher = pattern.matcher(seatNumber);
        int row = 0;
        if (matcher.matches()) {
            row = Integer.parseInt(matcher.group(1));
        } else {
            throw new IllegalArgumentException("Invalid seat number format: " + seatNumber);
        }

        // 1. Determine the final typeName string first
        String finalTypeName = "Economy"; // Default

        if (aircraft.getAircraftModel().contains("A320")) {
            if (row <= 4)
                finalTypeName = "Business";
            else if (row == 9 || row == 10)
                finalTypeName = "Exit Row";
            else if (row == 24 || row == 25)
                finalTypeName = "Limited Recline";
        } else if (aircraft.getAircraftModel().contains("777")) {
            if (row <= 8)
                finalTypeName = "Business";
            else if (row >= 10 && row <= 14)
                finalTypeName = "Premium Economy";
            else if (row == 15)
                finalTypeName = "Exit Row";
        }
        // Add more aircraft types here...
        String TypeName = finalTypeName;
        // Find the SeatType entity by name
        return seatTypeRepository.findByTypeNameIgnoreCase(finalTypeName)
                .orElseThrow(() -> new IllegalStateException("SeatType not found in database: " + TypeName));
    }

    /**
     * Calculates the final price based on flight base price and seat type rules.
     */
    private BigDecimal calculatePrice(Flight flight, SeatType seatType) {
        Hibernate.initialize(flight.getAirline()); // Ensure airline is loaded

        // Find the best matching pricing rule (airline-specific or general)
        PricingRule rule = pricingRuleRepository.findBestMatch(flight.getAirline(), seatType)
                .orElse(null); // Find the most specific rule

        BigDecimal multiplier = BigDecimal.ONE; // Default multiplier is 1.0
        if (rule != null) {
            multiplier = rule.getPriceMultiplier();
        } else {
            // Fallback: Try finding a general rule if no specific/general match found by
            // findBestMatch (shouldn't happen with proper setup)
            PricingRule generalRule = pricingRuleRepository.findByAirlineIsNullAndSeatType(seatType).orElse(null);
            if (generalRule != null) {
                multiplier = generalRule.getPriceMultiplier();
            } else {
                System.err.println(
                        "WARN: No pricing rule found for SeatType: " + seatType.getTypeName() + ". Using base price.");
            }
        }

        return flight.getBasePrice().multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }

    private boolean isSeatTaken(Flight flight, String seatNumber) {
        Hibernate.initialize(flight.getBookings()); // Ensure bookings are loaded
        return flight.getBookings().stream()
                .filter(b -> "CONFIRMED".equalsIgnoreCase(b.getStatus())
                        || "CHECKED-IN".equalsIgnoreCase(b.getStatus()))
                .anyMatch(b -> seatNumber.equalsIgnoreCase(b.getSeatNo()));
    }

    private String generatePnr() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
