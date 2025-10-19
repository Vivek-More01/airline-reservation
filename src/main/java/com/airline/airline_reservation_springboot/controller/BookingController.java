package com.airline.airline_reservation_springboot.controller;

import com.airline.airline_reservation_springboot.dto.BookingConfirmationDTO;
// import com.airline.airline_reservation_springboot.model.Booking;
// import com.airline.airline_reservation_springboot.model.Flight;
// import com.airline.airline_reservation_springboot.model.User;
import com.airline.airline_reservation_springboot.service.BookingService;
// import com.airline.airline_reservation_springboot.service.FlightService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

@Controller
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Displays the booking confirmation page for a specific booking.
     * This is called AFTER the API successfully creates the booking and the browser
     * redirects.
     * 
     * @param bookingId The ID of the booking to display, taken from the URL.
     * @param model     The Spring model to pass data to the view.
     * @return The name of the confirmation HTML template.
     */
    @GetMapping("/booking-confirmation/{bookingId}")
    public String showBookingConfirmation(@PathVariable("bookingId") Integer bookingId, Model model) {
        // Call the new service method that returns the DTO
        Optional<BookingConfirmationDTO> dtoOpt = bookingService.getBookingConfirmationDetails(bookingId);

        if (dtoOpt.isPresent()) {
            // Add the clean DTO to the model
            model.addAttribute("booking", dtoOpt.get());
            return "booking-confirmation";
        } else {
            return "redirect:/";
        }
    }
}