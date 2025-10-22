package com.airline.airline_reservation_springboot.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMsg = "An unexpected error occurred.";
        Integer statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(); // Default to 500

        if (status != null) {
            statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMsg = "Oops! The page you're looking for doesn't exist.";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorMsg = "Something went wrong on our end. Please try again later.";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorMsg = "Sorry, you don't have permission to access this page.";
            }
        }

        // You can extract more details if needed, e.g., the exception message
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        String exceptionMessage = (exception instanceof Exception) ? ((Exception) exception).getMessage() : "N/A";

        // Add attributes to the model for the Thymeleaf template
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMsg);
        model.addAttribute("exceptionMessage", exceptionMessage); // For debugging purposes

        return "error"; // Name of the HTML template file (error.html)
    }

    // Note: The getErrorPath method is deprecated and no longer needed in recent
    // Spring Boot versions.
    // Spring Boot automatically maps requests to /error to the handler method
    // above.
}
