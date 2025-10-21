package com.airline.airline_reservation_springboot.controller;

import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.service.FlightService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin") // All methods in this class will be under the /admin path
public class AdminController {

    private final FlightService flightService;

    public AdminController(FlightService flightService) {
        this.flightService = flightService;
    }

    /**
     * Displays the main admin dashboard, which shows a list of all flights.
     * 
     * @param model The Spring model to pass data to the view.
     * @return The name of the admin dashboard HTML template.
     */
    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        List<Flight> allFlights = flightService.findAllFlights();
        model.addAttribute("flights", allFlights);
        return "admin/dashboard"; // This will look for a dashboard.html inside a new 'admin' folder
    }

    /**
     * Shows the form for adding a new flight.
     * 
     * @param model The Spring model, which will hold a new Flight object for the
     *              form.
     * @return The name of the add-flight HTML template.
     */
    @GetMapping("/flights/add")
    public String showAddFlightForm(Model model) {
        model.addAttribute("flight", new Flight());
        return "admin/add-flight";
    }

    /**
     * Processes the submission of the add flight form.
     * 
     * @param flight The Flight object populated with data from the form.
     * @return A redirect to the admin dashboard.
     */
    @PostMapping("/flights/add")
    public String processAddFlight(@ModelAttribute Flight flight) {
        flightService.saveFlight(flight);
        return "redirect:/admin/dashboard";
    }
}
