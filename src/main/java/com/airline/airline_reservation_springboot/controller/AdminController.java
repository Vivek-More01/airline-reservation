package com.airline.airline_reservation_springboot.controller;

import com.airline.airline_reservation_springboot.dto.AnalyticsSummaryDTO;
import com.airline.airline_reservation_springboot.dto.BookingSummaryDTO;
import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.model.User;
import com.airline.airline_reservation_springboot.service.BookingService;
import com.airline.airline_reservation_springboot.service.FlightService;
import com.airline.airline_reservation_springboot.service.UserService;
import com.airline.airline_reservation_springboot.service.AnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin") // All methods in this class will be under the /admin path
public class AdminController {

    private final FlightService flightService;
    UserService userService;
    BookingService bookingService;
    private final AnalyticsService analyticsService;

    public AdminController(FlightService flightService, UserService userService, BookingService bookingService, AnalyticsService analyticsService) {
        this.flightService = flightService;
        this.userService = userService;
        this.bookingService = bookingService;
        this.analyticsService = analyticsService;

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

    // @GetMapping("/users/bookings")
    // public String showUserBookingSearchPage() {
    //     return "admin/user-booking-search"; // Points to templates/admin/user-booking-search.html
    // }

    // /**
    //  * Handles the user search request and displays the booking history.
    //  * @param email The email address entered by the admin.
    //  * @param model The Spring model.
    //  * @param redirectAttributes Used for error messages if user not found.
    //  * @return The path to the results template or a redirect if user not found.
    //  */
    // @GetMapping("/users/bookings/results")
    // public String showUserBookingResults(@RequestParam("email") String email, Model model, RedirectAttributes redirectAttributes) {
    //     Optional<User> userOpt = userService.findByEmail(email);

    //     if (userOpt.isPresent()) {
    //         User user = userOpt.get();
    //         List<BookingSummaryDTO> bookings = bookingService.findBookingsByUser(user);
    //         model.addAttribute("searchedUser", user); // Pass the found user details
    //         model.addAttribute("bookings", bookings); // Pass the list of booking summaries
    //         model.addAttribute("searchEmail", email); // Pass email back for display
    //         return "admin/user-booking-results"; // Points to templates/admin/user-booking-results.html
    //     } else {
    //         // User not found, redirect back to search page with an error message
    //         redirectAttributes.addFlashAttribute("errorMessage", "No user found with email: " + email);
    //         return "redirect:/admin/users/bookings";
    //     }
    // }

    // --- METHODS FOR FLIGHT STATUS MANAGEMENT ---

    /**
     * Handles the request to cancel a specific flight.
     */
    @PostMapping("/flights/{flightId}/cancel")
    public String cancelFlight(@PathVariable("flightId") Integer flightId, RedirectAttributes redirectAttributes) {
        boolean success = flightService.cancelFlight(flightId);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Flight ID " + flightId + " cancelled successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not cancel Flight ID " + flightId + ". It might not exist.");
        }
        return "redirect:/admin/dashboard";
    }

    /**
     * Handles the request to delay a specific flight.
     * Expects new departure and arrival times from form parameters.
     */
    @PostMapping("/flights/{flightId}/delay")
    public String delayFlight(@PathVariable("flightId") Integer flightId,
                              @RequestParam("newDeparture") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newDeparture,
                              @RequestParam("newArrival") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newArrival,
                              RedirectAttributes redirectAttributes) {
        boolean success = flightService.delayFlight(flightId, newDeparture, newArrival);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Flight ID " + flightId + " delayed successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not delay Flight ID " + flightId + ". It might not exist or is already cancelled.");
        }
        return "redirect:/admin/dashboard";
    }

    /**
     * Displays the staff management page, listing all current staff members.
     */
    @GetMapping("/staff/manage")
    public String showStaffManagementPage(Model model) {
        // Find users with the 'AirlineStaff' role
        List<User> staffMembers = userService.findUsersByRole("Staff");
        model.addAttribute("staffMembers", staffMembers);
        return "admin/manage-staff"; // Points to templates/admin/manage-staff.html
    }

    /**
     * Shows the form for adding a new staff member.
     */
    @GetMapping("/staff/add")
    public String showAddStaffForm(Model model) {
        model.addAttribute("staffUser", new User()); // Use User object for the form binding
        return "admin/add-staff"; // Points to templates/admin/add-staff.html
    }

    /**
     * Processes the submission for adding a new staff member.
     */
    @PostMapping("/staff/add")
    public String processAddStaff(@ModelAttribute("staffUser") User staffUser, BindingResult result, RedirectAttributes redirectAttributes) {
        // Basic validation example (can add more using @Valid and DTOs)
        if (userService.findByEmail(staffUser.getEmail()).isPresent()) {
            result.rejectValue("email", "error.staffUser", "Email address already exists.");
        }
        if (staffUser.getPassword() == null || staffUser.getPassword().length() < 6) { // Example password length check
             result.rejectValue("password", "error.staffUser", "Password must be at least 6 characters.");
        }

        if (result.hasErrors()) {
            return "admin/add-staff"; // Return to form if errors exist
        }

        // Set the role and save using the existing registerUser (which handles hashing)
        staffUser.setRole("AirlineStaff");
        userService.registerUser(staffUser); // Use registerUser to handle hashing and default status

        redirectAttributes.addFlashAttribute("successMessage", "Staff member added successfully.");
        return "redirect:/admin/staff/manage";
    }

    /**
     * Handles the request to remove (soft delete) a staff member.
     */
    @PostMapping("/staff/{userId}/remove")
    public String removeStaffMember(@PathVariable("userId") Integer userId, RedirectAttributes redirectAttributes) {
        // Optional: Add check to prevent deleting the last admin or self-deletion
        boolean success = userService.deleteUser(userId); // Uses the soft delete method

        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Staff member removed successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not remove staff member. User not found.");
        }
        return "redirect:/admin/staff/manage";
    }

    /**
     * Displays the passenger management page.
     * Optionally filters by email and shows booking history if email parameter is
     * present.
     */
    @GetMapping("/passengers/manage")
    public String showPassengerManagementPage(@RequestParam(name = "searchEmail", required = false) String searchEmail,
            Model model) {

        if (searchEmail != null && !searchEmail.trim().isEmpty()) {
            // --- Search Mode ---
            Optional<User> userOpt = userService.findByEmail(searchEmail);
            if (userOpt.isPresent() && "Passenger".equalsIgnoreCase(userOpt.get().getRole())) {
                User passenger = userOpt.get();
                List<BookingSummaryDTO> bookings = bookingService.findBookingsByUser(passenger);
                model.addAttribute("searchedPassenger", passenger);
                model.addAttribute("bookings", bookings);
            } else {
                model.addAttribute("errorMessage", "No passenger found with email: " + searchEmail);
                // Optionally show all passengers again as fallback
                List<User> allPassengers = userService.findUsersByRole("Passenger");
                model.addAttribute("passengers", allPassengers);
            }
            model.addAttribute("searchEmail", searchEmail); // Keep search term in input
        } else {
            // --- Default List Mode ---
            List<User> allPassengers = userService.findUsersByRole("Passenger");
            model.addAttribute("passengers", allPassengers);
        }

        return "admin/manage-passengers"; // Points to the single template
    }

    @PostMapping("/passengers/{userId}/ban")
    public String banPassenger(@PathVariable("userId") Integer userId, RedirectAttributes redirectAttributes) {
        boolean success = userService.banUser(userId);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Passenger banned successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not ban passenger. User not found.");
        }
        return "redirect:/admin/passengers/manage"; // Redirect back to the combined page
    }

    @PostMapping("/passengers/{userId}/unban")
    public String unbanPassenger(@PathVariable("userId") Integer userId, RedirectAttributes redirectAttributes) {
        boolean success = userService.unbanUser(userId);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Passenger unbanned successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Could not unban passenger. User not found.");
        }
        return "redirect:/admin/passengers/manage"; // Redirect back to the combined page
    }

    /**
     * Displays the analytics dashboard page.
     */
    @GetMapping("/analytics")
    public String showAnalyticsPage(Model model) {
        AnalyticsSummaryDTO summary = analyticsService.getAnalyticsSummary();
        model.addAttribute("summary", summary);
        return "admin/analytics"; // Points to templates/admin/analytics.html
    }
}
