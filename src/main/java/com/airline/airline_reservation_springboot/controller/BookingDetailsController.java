package com.airline.airline_reservation_springboot.controller;

import com.airline.airline_reservation_springboot.dto.PassengerDTO;
import com.airline.airline_reservation_springboot.model.Booking;
import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.model.Passenger;
import com.airline.airline_reservation_springboot.model.User;
import com.airline.airline_reservation_springboot.service.BookingService;
import com.airline.airline_reservation_springboot.service.FlightService;
import com.airline.airline_reservation_springboot.service.UserService; // Import UserService
// import jakarta.servlet.http.HttpSession; // Keep HttpSession for user check
import org.springframework.security.core.Authentication; // Keep Authentication
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class BookingDetailsController {

    private final FlightService flightService;
    private final BookingService bookingService;
    private final UserService userService; // Inject UserService

    public BookingDetailsController(FlightService flightService, BookingService bookingService,
            UserService userService) {
        this.flightService = flightService;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    /** Helper to get current user */
    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated())
            return null;
        String userEmail = authentication.getName();
        return userService.findByEmail(userEmail).orElse(null);
    }

    /**
     * Shows the form to collect details for multiple passengers.
     * Receives flightId and selected seatNumbers from the React app redirect.
     */
    @GetMapping("/booking/passenger-details")
    public String showPassengerDetailsForm(@RequestParam("flightId") Integer flightId,
            @RequestParam("seatNumbers") List<String> seatNumbers,
            Authentication authentication, Model model, RedirectAttributes redirectAttributes) {

        User currentUser = getCurrentUser(authentication);
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to continue booking.");
            return "redirect:/login"; // Redirect to login if not authenticated
        }

        Optional<Flight> flightOpt = flightService.findById(flightId);
        if (flightOpt.isEmpty() || !"Scheduled".equalsIgnoreCase(flightOpt.get().getStatus())
                && !"Delayed".equalsIgnoreCase(flightOpt.get().getStatus())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid or unavailable flight selected.");
            return "redirect:/"; // Redirect home if flight invalid
        }
        if (seatNumbers == null || seatNumbers.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "No seats selected.");
            return "redirect:/"; // Redirect home if no seats passed
        }

        // Prepare the form-backing object
        PassengerDTO form = new PassengerDTO();
        form.setFlightId(flightId);
        form.setSeatNumbers(seatNumbers);
        form.ensurePassengerListSize(); // Create empty Passenger objects for the form

        model.addAttribute("passengerDetailsForm", form);
        model.addAttribute("flight", flightOpt.get()); // Pass flight details for display
        return "passenger-details"; // templates/passenger-details.html
    }

    /**
     * Processes the submission of passenger details and creates multiple bookings.
     */
    @PostMapping("/booking/process-multiple")
    public String processMultipleBookings(@ModelAttribute PassengerDTO passengerDetailsForm,
            BindingResult result, // For potential validation
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User currentUser = getCurrentUser(authentication);
        if (currentUser == null) {
            return "redirect:/login"; // Should not happen if form was shown, but good check
        }

        // Add validation for passenger details here if needed using BindingResult

        List<Booking> createdBookings = new ArrayList<>();
        try {
            // Loop through each passenger detail and corresponding seat number
            for (int i = 0; i < passengerDetailsForm.getSeatNumbers().size(); i++) {
                String seatNumber = passengerDetailsForm.getSeatNumbers().get(i);
                Passenger passengerDetails = passengerDetailsForm.getPassengers().get(i);

                // In this simple model, we create a new Passenger record for each booking.
                // A more complex model might try to find/reuse existing Passenger records.
                // The CascadeType on Booking->Passenger handles saving the new Passenger.

                // Create booking using the service (service handles price, seat type, etc.)
                Booking newBooking = bookingService.createBooking(
                        currentUser, // The user making the booking
                        passengerDetailsForm.getFlightId(),
                        seatNumber,
                        passengerDetails // Pass the passenger details
                );
                createdBookings.add(newBooking);
            }

            // If all bookings succeeded, redirect to a multi-booking confirmation page (or
            // back to My Bookings)
            // Pass PNRs or IDs if needed for the confirmation page
            List<String> pnrs = createdBookings.stream().map(Booking::getPnr).collect(Collectors.toList());
            redirectAttributes.addFlashAttribute("successMessage",
                    "Bookings successful! PNRs: " + String.join(", ", pnrs));
            return "redirect:/my-bookings"; // Redirect to user's booking list

        } catch (IllegalStateException | IllegalArgumentException e) {
            // Handle errors during booking (e.g., seat taken, flight invalid)
            redirectAttributes.addFlashAttribute("errorMessage", "Booking failed: " + e.getMessage());
            // Redirect back to the form, repopulating it requires more effort
            // For simplicity, redirect back to flight search or seat selection
            String params = passengerDetailsForm.getSeatNumbers().stream()
                    .map(sn -> "seatNumbers=" + sn)
                    .collect(Collectors.joining("&"));
            return "redirect:/booking/passenger-details?flightId=" + passengerDetailsForm.getFlightId() + "&" + params;

        } catch (Exception e) {
            // Catch unexpected errors
            redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred during booking.");
            return "redirect:/";
        }
    }
}
