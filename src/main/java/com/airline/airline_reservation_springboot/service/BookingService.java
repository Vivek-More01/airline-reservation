package com.airline.airline_reservation_springboot.service;

import com.airline.airline_reservation_springboot.dto.BookingConfirmationDTO;
import com.airline.airline_reservation_springboot.dto.BookingSummaryDTO;
import com.airline.airline_reservation_springboot.model.Booking;
import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.model.User;
import com.airline.airline_reservation_springboot.repository.BookingRepository;
import com.airline.airline_reservation_springboot.repository.FlightRepository;
// import com.airline.airline_reservation_springboot.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;


    public BookingService(BookingRepository bookingRepository, FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
    }

    @Transactional
    public Booking createBooking(User user, int flightId, String seatNumber) {
        // 1. Find the flight the user wants to book.
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid flight ID: " + flightId));

        // User user = userRepository.findById(userId)
        //         .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));

        // 2. Check if there are any seats available at all.
        if (flight.getSeatsAvailable() <= 0) {
            throw new IllegalStateException("No seats available on this flight.");
        }

        // 3. Check if the specific seat is already booked.
        boolean isSeatTaken = flight.getBookings().stream()
                .anyMatch(booking -> seatNumber.equalsIgnoreCase(booking.getSeat()) && booking.getStatus().equals("CONFIRMED"));

        if (isSeatTaken) {
            throw new IllegalStateException("Seat " + seatNumber + " is already taken.");
        }

        // 4. Update the flight's seat count and save it.
        flight.setSeatsAvailable(flight.getSeatsAvailable() - 1);
        flightRepository.save(flight);

        // 5. Create the new booking object.
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setSeat(seatNumber); // Set the user-chosen seat number
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus("CONFIRMED");
        // Generate a unique Passenger Name Record (PNR)
        booking.setPnr(generatePnr());

        // 6. Save and return the new booking.
        return bookingRepository.save(booking);
    }

    public Optional<Booking> findById(Integer bookingId) {
        return bookingRepository.findById(bookingId);
    }

    @Transactional(readOnly = true)
    public Optional<BookingConfirmationDTO> getBookingConfirmationDetails(Integer bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);

        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            // Because this method is transactional, we can safely access the lazy-loaded user and flight objects.
            User user = booking.getUser();
            Flight flight = booking.getFlight();

            // Manually build the DTO with all the data we need.
            BookingConfirmationDTO dto = new BookingConfirmationDTO(
                booking.getPnr(),
                booking.getSeat(),
                booking.getBookingDate(),
                user.getName(),
                flight.getAirline(),
                flight.getSource(),
                flight.getDestination()
            );
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public List<BookingSummaryDTO> findBookingsByUser(User user) {
        List<Booking> bookings = bookingRepository.findByUserOrderByBookingDateDesc(user); // Assumes you add this method to BookingRepository
        return bookings.stream()
                .map(booking -> new BookingSummaryDTO(
                        booking.getBookingId(),
                        booking.getFlight().getAirline(),
                        booking.getFlight().getSource(),
                        booking.getFlight().getDestination(),
                        booking.getFlight().getDeparture(),
                        booking.getSeat(),
                        booking.getStatus(),
                        booking.getPnr()))
                .collect(Collectors.toList());
    }

    /**
     * Cancels a booking if it belongs to the specified user and is cancellable.
     * @return true if cancellation was successful, false otherwise.
     */
    @Transactional
    public boolean cancelBooking(Integer bookingId, User user) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);

        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            // Security check: Ensure the booking belongs to the current user
            if (booking.getUser().getUserId() != user.getUserId()) {
                return false; // Or throw an AccessDeniedException
            }

            // Check if booking is already cancelled or in a non-cancellable state
            if (!"CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
                 return false; // Cannot cancel
            }

            // Perform cancellation logic
            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);

            // Increment available seats on the flight
            Flight flight = booking.getFlight();
            flight.setSeatsAvailable(flight.getSeatsAvailable() + 1);
            flightRepository.save(flight);

            return true;
        }
        return false; // Booking not found
    }

    /**
     * Checks in a passenger by updating their booking status.
     * @param bookingId The ID of the booking to check in.
     * @return true if successful, false if booking not found or not in 'CONFIRMED' state.
     */
    @Transactional
    public boolean checkInPassenger(Integer bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            System.out.println("Checking in booking ID: " + bookingId + " with status: " + booking.getStatus());
            // Only allow check-in for confirmed bookings
            if ("CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
                System.out.println("Checking in booking ID: " + bookingId);
                booking.setStatus("CHECKED-IN");
                System.out.println("Booking ID: " + bookingId + " is now checked in. New status: " + booking.getStatus());
                bookingRepository.save(booking);
                return true;
            }
        }
        return false; // Booking not found or not in correct state
    }

    /**
     * Allows staff/admin to cancel any booking.
     * 
     * @param bookingId The ID of the booking to cancel.
     * @return true if cancellation was successful, false otherwise.
     */
    @Transactional
    public boolean cancelBookingByStaff(Integer bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            // Staff might be able to cancel CONFIRMED or CHECKED-IN bookings
            if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
                return true; // Already cancelled
            }
            return performCancellation(booking); // Use shared logic
        }
        return false; // Booking not found
    }

    /** Shared logic for performing a booking cancellation. */
    private boolean performCancellation(Booking booking) {
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        // Increment available seats ONLY if the flight itself isn't cancelled
        Flight flight = booking.getFlight();
        if (!"Cancelled".equalsIgnoreCase(flight.getStatus())) {
            flight.setSeatsAvailable(flight.getSeatsAvailable() + 1);
            flightRepository.save(flight);
        }
        // Add refund logic / notifications here in a real app
        return true;
    }

    private String generatePnr() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
