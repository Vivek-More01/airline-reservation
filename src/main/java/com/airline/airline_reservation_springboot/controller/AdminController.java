package com.airline.airline_reservation_springboot.controller;

import com.airline.airline_reservation_springboot.dto.AnalyticsSummaryDTO;
import com.airline.airline_reservation_springboot.dto.BookingSummaryDTO;
import com.airline.airline_reservation_springboot.dto.FlightSummaryDTO; // Import DTO
import com.airline.airline_reservation_springboot.dto.UserDTO;
import com.airline.airline_reservation_springboot.model.Airline; // Import Airline
import com.airline.airline_reservation_springboot.model.Aircraft; // Import Aircraft
import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.model.User;
import com.airline.airline_reservation_springboot.repository.AirlineRepository; // Import Repos
import com.airline.airline_reservation_springboot.repository.AircraftRepository; // Import Repos
import com.airline.airline_reservation_springboot.service.AnalyticsService;
import com.airline.airline_reservation_springboot.service.BookingService;
import com.airline.airline_reservation_springboot.service.FlightService;
import com.airline.airline_reservation_springboot.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
// import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final FlightService flightService;
    private final UserService userService;
    private final BookingService bookingService;
    private final AnalyticsService analyticsService;
    private final AirlineRepository airlineRepository; // Inject Airline Repository
    private final AircraftRepository aircraftRepository; // Inject Aircraft Repository

    // Update Constructor
    public AdminController(FlightService flightService, UserService userService, BookingService bookingService,
            AnalyticsService analyticsService, AirlineRepository airlineRepository,
            AircraftRepository aircraftRepository) {
        this.flightService = flightService;
        this.userService = userService;
        this.bookingService = bookingService;
        this.analyticsService = analyticsService;
        this.airlineRepository = airlineRepository; // Assign Repo
        this.aircraftRepository = aircraftRepository; // Assign Repo
    }

    // --- UPDATED Dashboard: Uses DTO method ---
    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        // Use the service method that already returns DTOs
        List<FlightSummaryDTO> flightSummaries = flightService.getAllFlightSummaries();
        model.addAttribute("flights", flightSummaries);
        return "admin/dashboard";
    }

    // --- UPDATED Add Flight Form: Add airlines/aircraft lists ---
    @GetMapping("/flights/add")
    public String showAddFlightForm(Model model) {
        // Provide lists for dropdowns in the form
        List<Airline> airlines = airlineRepository.findAll();
        List<Aircraft> aircrafts = aircraftRepository.findAll();
        model.addAttribute("airlines", airlines);
        model.addAttribute("aircrafts", aircrafts);

        // Add empty Flight object for form binding
        Flight newFlight = new Flight();
        model.addAttribute("flight", newFlight);

        return "admin/add-flight";
    }

    // --- UPDATED Add Flight Processing: Handle related entities ---
    @PostMapping("/flights/add")
    public String processAddFlight(@ModelAttribute Flight flight,
            // Get IDs from form submission
            @RequestParam("airlineId") Integer airlineId,
            @RequestParam("aircraftId") Integer aircraftId,
            BindingResult result, // Add BindingResult for potential errors
            RedirectAttributes redirectAttributes) {

        // Fetch the actual Airline and Aircraft entities
        Optional<Airline> airlineOpt = airlineRepository.findById(airlineId);
        Optional<Aircraft> aircraftOpt = aircraftRepository.findById(aircraftId);

        if (airlineOpt.isEmpty() || aircraftOpt.isEmpty()) {
            // Handle error - Airline or Aircraft not found
            redirectAttributes.addFlashAttribute("errorMessage", "Selected Airline or Aircraft not found.");
            // Optionally add lists back to model and return to form
            // model.addAttribute("airlines", airlineRepository.findAll()); ...
            // return "admin/add-flight";
            return "redirect:/admin/flights/add"; // Simple redirect for now
        }

        // Set the fetched entities on the Flight object
        flight.setAirline(airlineOpt.get());
        flight.setAircraft(aircraftOpt.get());

        // Set available seats based on the selected aircraft's total seats
        flight.setSeatsAvailable(aircraftOpt.get().getTotalSeats());
        flight.setStatus("Scheduled"); // Default status

        // Add further validation if needed using BindingResult

        flightService.saveFlight(flight);
        redirectAttributes.addFlashAttribute("successMessage", "Flight added successfully!");
        return "redirect:/admin/dashboard";
    }

    // --- Passenger/Staff/Status methods remain largely the same, relying on
    // services ---
    // (Ensure they handle potential lazy loading issues if not using DTOs)

    @GetMapping("/users/bookings") // Renamed from passengers/manage search part
    public String showUserBookingSearchPage() {
        return "admin/user-booking-search"; // Keep separate search page for now
    }

    @GetMapping("/users/bookings/results") // Renamed from passengers/manage results part
    public String showUserBookingResults(@RequestParam("email") String email, Model model,
            RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<BookingSummaryDTO> bookings = bookingService.findBookingsByUser(user); // Service returns DTOs
            model.addAttribute("searchedUser", userService.convertToDTO(user)); // Pass UserDTO
            model.addAttribute("bookings", bookings);
            model.addAttribute("searchEmail", email);
            return "admin/user-booking-results"; // Keep separate results page for now
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "No user found with email: " + email);
            return "redirect:/admin/users/bookings";
        }
    }

    @PostMapping("/flights/{flightId}/cancel")
    public String cancelFlight(@PathVariable("flightId") Integer flightId, RedirectAttributes redirectAttributes) {
        boolean success = flightService.cancelFlight(flightId);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Flight canceled successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to cancel flight.");
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/flights/{flightId}/delay")
    public String delayFlight(@PathVariable("flightId") Integer flightId,
            @RequestParam("newDeparture") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newDeparture,
            @RequestParam("newArrival") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newArrival,
            RedirectAttributes redirectAttributes) {
        boolean success = flightService.delayFlight(flightId, newDeparture, newArrival);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Flight delayed successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delay flight.");
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/staff/manage")
    public String showStaffManagementPage(Model model) {
        List<User> staffMembers = userService.findUsersByRole("Staff");
        // Convert to DTOs before sending to view to avoid lazy issues
        List<UserDTO> staffDTOs = staffMembers.stream().map(userService::convertToDTO).collect(Collectors.toList());
        model.addAttribute("staffMembers", staffDTOs);
        return "admin/manage-staff";
    }

    @GetMapping("/staff/add")
    public String showAddStaffForm(Model model) {
        model.addAttribute("staffUser", new UserDTO()); // Use DTO for form binding
        return "admin/add-staff";
    }

    @PostMapping("/staff/add")
    public String processAddStaff(@ModelAttribute("staffUser") UserDTO staffDto, // Still use DTO for name/email
            @RequestParam("password") String password, // Get password separately
            BindingResult result, // For DTO validation errors
            Model model, // Add model to pass back password error
            RedirectAttributes redirectAttributes) {

        boolean hasErrors = false;
        // Check if email already exists
        if (userService.findByEmail(staffDto.getEmail()).isPresent()) {
            result.rejectValue("email", "error.staffUser", "Email address already exists.");
            hasErrors = true;
        }

        // Manually validate password
        String passwordError = null;
        if (password == null || password.length() < 6) {
            passwordError = "Password must be at least 6 characters.";
            hasErrors = true;
        }

        if (hasErrors || result.hasErrors()) {
            // Pass the password error back to the template if it exists
            if (passwordError != null) {
                model.addAttribute("passwordError", passwordError);
            }
            // Need to pass staffUser back in case of other errors for th:object
            model.addAttribute("staffUser", staffDto);
            return "admin/add-staff"; // Return to form
        }

        // Manually create User entity from DTO and password parameter
        User newUser = new User();
        newUser.setName(staffDto.getName());
        newUser.setEmail(staffDto.getEmail());
        newUser.setPassword(password); // Set the plain password (service will hash it)
        newUser.setAccountStatus("ACTIVE");
        // newUser.setAccountStatus("ACTIVE"); // registerUser sets this

        userService.registerUser(newUser, "Staff"); // registerUser handles hashing

        redirectAttributes.addFlashAttribute("successMessage", "Staff member added successfully.");
        return "redirect:/admin/staff/manage";
    }
    @PostMapping("/staff/{userId}/remove")
    public String removeStaffMember(@PathVariable("userId") Integer userId, RedirectAttributes redirectAttributes) {
        // boolean success = userService.deleteUser(userId);
        // ... messages ...
        return "redirect:/admin/staff/manage";
    }

    @GetMapping("/passengers/manage")
    public String showPassengerManagementPage(@RequestParam(name = "searchEmail", required = false) String searchEmail,
            Model model) {
        if (searchEmail != null && !searchEmail.trim().isEmpty()) {
            Optional<User> userOpt = userService.findByEmail(searchEmail);
            if (userOpt.isPresent() && "Passenger".equalsIgnoreCase(userOpt.get().getRole())) {
                User passenger = userOpt.get();
                List<BookingSummaryDTO> bookings = bookingService.findBookingsByUser(passenger);
                model.addAttribute("searchedPassenger", userService.convertToDTO(passenger)); // Pass DTO
                model.addAttribute("bookings", bookings);
            } else {
                model.addAttribute("errorMessage", "No passenger found with email: " + searchEmail);
                List<User> allPassengers = userService.findUsersByRole("Passenger");
                model.addAttribute("passengers",
                        allPassengers.stream().map(userService::convertToDTO).collect(Collectors.toList())); // Pass
                                                                                                             // List of
                                                                                                             // DTOs
            }
            model.addAttribute("searchEmail", searchEmail);
        } else {
            List<User> allPassengers = userService.findUsersByRole("Passenger");
            model.addAttribute("passengers",
                    allPassengers.stream().map(userService::convertToDTO).collect(Collectors.toList())); // Pass List of
                                                                                                         // DTOs
        }
        return "admin/manage-passengers";
    }

    @PostMapping("/passengers/{userId}/ban")
    public String banPassenger(@PathVariable("userId") Integer userId, RedirectAttributes redirectAttributes) {
        userService.banUser(userId);
        return "redirect:/admin/passengers/manage";
    }

    @PostMapping("/passengers/{userId}/unban")
    public String unbanPassenger(@PathVariable("userId") Integer userId, RedirectAttributes redirectAttributes) {
        boolean success = userService.unbanUser(userId);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Passenger unbanned successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to unban passenger.");
        }
        return "redirect:/admin/passengers/manage";
    }

    @GetMapping("/analytics")
    public String showAnalyticsPage(Model model) {
        AnalyticsSummaryDTO summary = analyticsService.getAnalyticsSummary();
        model.addAttribute("summary", summary);
        return "admin/analytics";
    }
}
