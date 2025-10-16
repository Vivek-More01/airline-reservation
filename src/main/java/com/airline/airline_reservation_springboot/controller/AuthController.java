package com.airline.airline_reservation_springboot.controller;

import com.airline.airline_reservation_springboot.model.User;
import com.airline.airline_reservation_springboot.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//chsange
import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Returns login.html
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String email, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<User> userOptional = userService.loginUser(email, password);

        if (userOptional.isPresent()) {
            session.setAttribute("user", userOptional.get());
            return "redirect:/"; // Redirect to home page on successful login
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid email or password");
            return "redirect:/login"; // Redirect back to login page on failure
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Returns register.html
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        userService.registerUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
        return "redirect:/login";
    }


    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.invalidate(); // Invalidate the session
        return "redirect:/login";
    }
}
