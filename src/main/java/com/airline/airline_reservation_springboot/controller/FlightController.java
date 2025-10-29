package com.airline.airline_reservation_springboot.controller;

import com.airline.airline_reservation_springboot.dto.FlightSummaryDTO;
// import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/")
    public String homeOrSpa(@RequestParam(name = "flightId", required = false) Integer flightId) {
        if (flightId != null) {
            // A flightId is present, assume user wants the seat selection SPA
            // Forward internally to the SPA's entry point HTML in the static folder
            return "forward:/app.html";
        } else {
            // No flightId, show the regular Thymeleaf homepage
            return "index";
        }
    }

    @GetMapping("/search")
    public String searchFlights(@RequestParam String source,
                                @RequestParam String destination,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
                                Model model) {

        List<FlightSummaryDTO> flights = flightService.searchFlights(source, destination, departureDate);
        model.addAttribute("flights", flights);
        return "flight-results"; // Returns flight-results.html
    }
}
