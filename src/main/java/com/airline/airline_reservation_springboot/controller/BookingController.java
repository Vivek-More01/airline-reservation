package com.airline.airline_reservation_springboot.controller;

import com.airline.airline_reservation_springboot.model.Booking;
import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.model.User;
import com.airline.airline_reservation_springboot.service.BookingService;
import com.airline.airline_reservation_springboot.service.FlightService;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookingController {

    private final BookingService bookingService;
    private final FlightService flightService;

    public BookingController(BookingService bookingService, FlightService flightService) {
        this.bookingService = bookingService;
        this.flightService = flightService;
    }

    @PostMapping("/book/{flightId}")
    public String bookFlight(@PathVariable("flightId") int flightId, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // 1. Check if user is logged in
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must be logged in to book a flight.");
            return "redirect:/login";
        }

        // 2. Find the flight the user wants to book
        Flight flightToBook = flightService.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid flight ID:" + flightId));

        try {
            // 3. Call the service to create the booking
            Booking newBooking = bookingService.createBooking(flightToBook, currentUser);
            System.out.println("Booking successful: " + newBooking);
            model.addAttribute("booking", newBooking);
            // Add the new booking object to flash attributes to show on the confirmation page
            redirectAttributes.addFlashAttribute("bookingDetails", newBooking);
            return "redirect:/booking-confirmation";

        } catch (IllegalStateException e) {
            // Handle case where no seats are available
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/search"; // Redirect back to search results with an error
        }
    }

    @GetMapping("/booking-confirmation")
    public String showBookingConfirmation(Model model) {
    // 1. Retrieve the entire Booking object using the correct key.
    Booking booking = (Booking) model.getAttribute("bookingDetails");

    // 2. Check if the booking object exists.
    if (booking == null) {
        System.out.println("Booking details not found in the model.");
        return "redirect:/";
    }

    // 3. No need to fetch from the database again, just add it back to the model
    // for the template to use (though it's already there).
    model.addAttribute("booking", booking);

    return "booking-confirmation";
    }
}
