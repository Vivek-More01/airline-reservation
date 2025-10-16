package com.airline.airline_reservation_springboot.controller;

import com.airline.airline_reservation_springboot.dto.BookingRequest;
import com.airline.airline_reservation_springboot.dto.FlightDetailsDTO;
import com.airline.airline_reservation_springboot.model.Booking;
import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.model.User;
import com.airline.airline_reservation_springboot.service.BookingService;
import com.airline.airline_reservation_springboot.service.FlightService;
import com.airline.airline_reservation_springboot.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api") // All endpoints in this controller will start with /api
public class ApiController {

    private final FlightService flightService;
    private final BookingService bookingService;
    private final UserService userService;

    public ApiController(FlightService flightService, BookingService bookingService, UserService userService) {
        this.flightService = flightService;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @GetMapping("/flights/{flightId}")
    public ResponseEntity<FlightDetailsDTO> getFlightDetails(@PathVariable int flightId) {
        FlightDetailsDTO flightDetails = flightService.getFlightDetails(flightId);

        if (flightDetails != null) {
            return ResponseEntity.ok(flightDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest, HttpSession session) {
        // 1. Get the currently logged-in user from the session.
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            // If no user is logged in, return an "Unauthorized" error.
            return new ResponseEntity<>("User not logged in.", HttpStatus.UNAUTHORIZED);
        }

        // 2. Refresh the user object from the database to ensure it's a managed entity.
        User managedUser = userService.findByEmail(currentUser.getEmail()).orElse(null);
        if (managedUser == null) {
            return new ResponseEntity<>("Invalid user session.", HttpStatus.UNAUTHORIZED);
        }

        try {
            // 3. Call the booking service with the user, flight ID, and seat number from the request.
            Booking newBooking = bookingService.createBooking(
                managedUser,
                bookingRequest.getFlightId(),
                bookingRequest.getSeatNumber()
            );
            // 4. On success, return the newly created booking object.
            return ResponseEntity.ok(newBooking);

        } catch (IllegalStateException e) {
            // If the booking fails (e.g., seat taken), return a "Bad Request" error with the message.
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
             // If the flight ID is invalid
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/me")
    public ResponseEntity<User> getCurrentUser(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            return ResponseEntity.ok(currentUser);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
