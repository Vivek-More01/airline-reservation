package com.airline.airline_reservation_springboot.controller;

import com.airline.airline_reservation_springboot.dto.FlightManifestDTO;
import com.airline.airline_reservation_springboot.dto.FlightSummaryDTO;
import com.airline.airline_reservation_springboot.service.BookingService;
// import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.service.FlightService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/staff")
public class StaffController {

    private final FlightService flightService;
    private final BookingService bookingService; // Inject BookingService

    // Update constructor
    public StaffController(FlightService flightService, BookingService bookingService) {
        this.flightService = flightService;
        this.bookingService = bookingService;
    }

    /**
     * Displays the main staff dashboard, listing summaries of all flights.
     */
    @GetMapping("/dashboard")
    public String showStaffDashboard(Model model) {
        // Call the new service method that returns DTOs
        List<FlightSummaryDTO> flightSummaries = flightService.getAllFlightSummaries();
        // Pass the list of DTOs to the model
        model.addAttribute("flights", flightSummaries);
        // System.out.println("Staff dashboard accessed. Number of flights: " + flightSummaries.size());
        return "staff/dashboard";
    }

    /**
     * Displays the passenger manifest (list of bookings) for a specific flight.
     */
    @GetMapping("/flights/{flightId}/manifest")
    public String showPassengerManifest(@PathVariable("flightId") Integer flightId, Model model) {
        Optional<FlightManifestDTO> manifestOpt = flightService.getFlightManifestDetails(flightId);
        System.out.println("Accessing manifest for flight ID: " + flightId);
        System.out.println("Manifest found: " + manifestOpt.isPresent());
        
        if (manifestOpt.isPresent()) {
            model.addAttribute("manifest", manifestOpt.get());
            System.out.println("Number of passengers: " + manifestOpt.get());
        } else {
            model.addAttribute("flightNotFound", true);
        }
        
        return "staff/manifest"; 
    }

    @PostMapping("/bookings/{bookingId}/checkin")
    public String checkInPassenger(@PathVariable("bookingId") Integer bookingId,
                                   @RequestParam("flightId") Integer flightId, // Get flightId to redirect back
                                   RedirectAttributes redirectAttributes) {
        boolean success = bookingService.checkInPassenger(bookingId);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Passenger checked in successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not check in passenger (invalid booking or status).");
        }
        // Redirect back to the manifest page for the same flight
        return "redirect:/staff/flights/" + flightId + "/manifest";
    }

    @PostMapping("/bookings/{bookingId}/cancel")
    public String cancelBookingByStaff(@PathVariable("bookingId") Integer bookingId,
                                       @RequestParam("flightId") Integer flightId, // Get flightId to redirect back
                                       RedirectAttributes redirectAttributes) {
        boolean success = bookingService.cancelBookingByStaff(bookingId);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Booking cancelled successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not cancel booking (invalid booking or status).");
        }
         // Redirect back to the manifest page for the same flight
        return "redirect:/staff/flights/" + flightId + "/manifest";
    }

    @PostMapping("/bookings/{bookingId}/change-seat")
    public String changeSeatNumber(@PathVariable("bookingId") Integer bookingId,
            @RequestParam("flightId") Integer flightId, // Needed for redirect
            @RequestParam("newSeatNumber") String newSeatNumber,
            RedirectAttributes redirectAttributes) {
        try {
            boolean success = bookingService.changeSeatNumber(bookingId, newSeatNumber);
            if (success) {
                redirectAttributes.addFlashAttribute("successMessage",
                        "Seat changed successfully to " + newSeatNumber + ".");
            } else {
                // This case might not be reachable if service throws exceptions
                redirectAttributes.addFlashAttribute("errorMessage", "Could not change seat.");
            }
        } catch (IllegalStateException | IllegalArgumentException e) {
            // Catch exceptions from the service layer (e.g., seat taken, invalid state)
            redirectAttributes.addFlashAttribute("errorMessage", "Seat Change Failed: " + e.getMessage());
        }
        // Redirect back to the manifest for the same flight
        return "redirect:/staff/flights/" + flightId + "/manifest";
    }
}
