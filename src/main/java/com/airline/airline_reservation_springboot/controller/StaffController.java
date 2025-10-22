package com.airline.airline_reservation_springboot.controller;

import com.airline.airline_reservation_springboot.dto.FlightManifestDTO;
import com.airline.airline_reservation_springboot.dto.FlightSummaryDTO;
// import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.service.FlightService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/staff")
public class StaffController {

    private final FlightService flightService;

    public StaffController(FlightService flightService) {
        this.flightService = flightService;
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
}
