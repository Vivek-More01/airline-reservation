package com.airline.airline_reservation_springboot.config;

import com.airline.airline_reservation_springboot.model.User;
import com.airline.airline_reservation_springboot.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    public CustomAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // Get the username (which is the email) from the authenticated principal
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        // Use the UserService to fetch the full User entity from the database
        User user = userService.findByEmail(username)
                .orElseThrow(() -> new IllegalStateException("User not found after successful login"));

        // Get the current session
        HttpSession session = request.getSession();

        // Place the full User object into the session with the key "user"
        session.setAttribute("user", user);

        // Redirect the user to the homepage
        response.sendRedirect("/");
    }
}
