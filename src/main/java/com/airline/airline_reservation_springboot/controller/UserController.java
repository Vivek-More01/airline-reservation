package com.airline.airline_reservation_springboot.controller;

import com.airline.airline_reservation_springboot.dto.BookingSummaryDTO;
import com.airline.airline_reservation_springboot.dto.UserDTO; // Ensure UserDTO is imported
import com.airline.airline_reservation_springboot.model.User;
import com.airline.airline_reservation_springboot.service.BookingService;
import com.airline.airline_reservation_springboot.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication; // Import Authentication
// import org.springframework.security.core.userdetails.UserDetails; // Import UserDetails
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
// import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, BookingService bookingService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Helper method to get the currently logged-in user entity.
     * Uses Spring Security's Authentication object.
     */
    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String userEmail = authentication.getName(); // Gets the username (email)
        // Fetch the full User entity from the database using the email
        return userService.findByEmail(userEmail).orElse(null);
    }

    // --- My Bookings ---

    @GetMapping("/my-bookings")
    // Use Authentication object instead of HttpSession
    public String showMyBookings(Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication); // Use helper method
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to view your bookings.");
            return "redirect:/login";
        }

        List<BookingSummaryDTO> bookings = bookingService.findBookingsByUser(currentUser);
        model.addAttribute("bookings", bookings);
        return "my-bookings";
    }

    @PostMapping("/bookings/{bookingId}/cancel")
    // Use Authentication object instead of HttpSession
    public String cancelBooking(@PathVariable("bookingId") Integer bookingId, Authentication authentication,
            RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication); // Use helper method
        if (currentUser == null) {
            return "redirect:/login";
        }

        boolean success = bookingService.cancelBooking(bookingId, currentUser);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Booking cancelled successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Could not cancel booking. It may already be cancelled or does not belong to you.");
        }
        return "redirect:/my-bookings";
    }

    // --- Account Settings ---

    @GetMapping("/account-settings")
    // Use Authentication object instead of HttpSession
    public String showAccountSettings(Authentication authentication, Model model,
            RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication); // Use helper method
        if (currentUser == null) {
            return "redirect:/login";
        }
        // Pass the DTO to the form
        model.addAttribute("userDto", userService.convertToDTO(currentUser));
        model.addAttribute("passwordChangeRequest", new PasswordChangeRequest());
        return "account-settings";
    }

    @PostMapping("/account-settings/update-profile")
    // Use Authentication object instead of HttpSession
    public String updateProfile(@ModelAttribute UserDTO userDto, Authentication authentication,
            RedirectAttributes redirectAttributes, HttpSession session) {
        User currentUser = getCurrentUser(authentication); // Use helper method
        if (currentUser == null)
            return "redirect:/login";

        // Update logic remains the same, using the fetched currentUser
        User userToUpdate = currentUser; // Already fetched the managed entity
        userToUpdate.setName(userDto.getName());

        if (!userToUpdate.getEmail().equalsIgnoreCase(userDto.getEmail())) {
            if (userService.findByEmail(userDto.getEmail()).isPresent()) {
                redirectAttributes.addFlashAttribute("profileError", "Email address already in use.");
                return "redirect:/account-settings";
            }
            userToUpdate.setEmail(userDto.getEmail());
            // userToUpdate.setUserId(userDto.getUserId()); // Assuming userId is same as email
            // System.out.println("Updated userId to: " + userDto.getUserId());
        }

        User updatedUser = userService.saveUser(userToUpdate);
        // IMPORTANT: Update session as the CustomAuthenticationSuccessHandler
        // originally set it.
        // If the header still relies on session.user, this is needed.
        session.setAttribute("user", updatedUser);
        redirectAttributes.addFlashAttribute("profileSuccess", "Profile updated successfully.");
        return "redirect:/account-settings";
    }

    @PostMapping("/account-settings/change-password")
    // Use Authentication object instead of HttpSession
    public String changePassword(@ModelAttribute PasswordChangeRequest passwordRequest, Authentication authentication,
            RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(authentication); // Use helper method
        if (currentUser == null)
            return "redirect:/login";

        // Update logic remains the same, using the fetched currentUser
        User userToUpdate = currentUser;

        if (!passwordEncoder.matches(passwordRequest.getCurrentPassword(), userToUpdate.getPassword())) {
            redirectAttributes.addFlashAttribute("passwordError", "Incorrect current password.");
            return "redirect:/account-settings";
        }

        if (!passwordRequest.getNewPassword().equals(passwordRequest.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("passwordError", "New passwords do not match.");
            return "redirect:/account-settings";
        }

        userToUpdate.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        userService.saveUser(userToUpdate);

        redirectAttributes.addFlashAttribute("passwordSuccess", "Password changed successfully.");
        return "redirect:/account-settings";
    }

    // Simple helper class for password change form (remains the same)
    public static class PasswordChangeRequest {
        private String currentPassword;
        private String newPassword;
        private String confirmPassword;

        // Getters and Setters
        public String getCurrentPassword() {
            return currentPassword;
        }

        public void setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }
    }
}
