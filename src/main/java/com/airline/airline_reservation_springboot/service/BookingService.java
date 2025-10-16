package com.airline.airline_reservation_springboot.service;

import com.airline.airline_reservation_springboot.model.Booking;
import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.model.User;
import com.airline.airline_reservation_springboot.repository.BookingRepository;
import com.airline.airline_reservation_springboot.repository.FlightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;

    public BookingService(BookingRepository bookingRepository, FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
    }

    @Transactional // Ensures the entire method is one single database transaction
    public Booking createBooking(Flight flight, User user) {
        // 1. Check if seats are available
        if (flight.getSeatsAvailable() <= 0) {
            throw new IllegalStateException("No seats available for this flight.");
        }

        // 2. Decrease the number of available seats and update the flight
        flight.setSeatsAvailable(flight.getSeatsAvailable() - 1);
        flightRepository.save(flight);

        // 3. Create a new booking record
        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setUser(user);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus("CONFIRMED");
        booking.setSeat("Seat-" + (flight.getSeatsTotal() - flight.getSeatsAvailable())); // Simple seat assignment
        // Generate a unique Passenger Name Record (PNR)
        booking.setPnr(UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        return bookingRepository.save(booking);
    }

    // public Optional<Booking> findById(int id) {
    //     return bookingRepository.findById(id);
    // }
}
