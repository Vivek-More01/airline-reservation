package com.airline.airline_reservation_springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Forwards requests that don't match known API endpoints or static files
 * to the root path ("/"), letting the SPA (React) handle routing.
 * IMPORTANT: Ensure this controller's mapping doesn't conflict with your API
 * paths (e.g., /api/**)
 * or static resource paths. Adjust the @RequestMapping if necessary.
 */
@Controller
public class SpaForwardingController {

    /**
     * Forwards any path that isn't matched by other controllers or static
     * resources.
     * The regex `^(?!/api|/css|/js|/images|/favicon.ico).*$` ensures API calls and
     * known static folders are excluded.
     * Adjust the regex pattern based on your API prefix and static resource paths.
     * It maps unmatched GET requests to the root index.html.
     */
    @RequestMapping(value = "{path:^(?!/api|/css|/js|/images|/favicon.ico|/actuator).*$}/**")
    public String forwardSpa() {
        // Forward to the root path where index.html is served
        return "forward:/app.html";
    }

    // You might also need a mapping specifically for the root if Spring Security
    // isn't handling it
     @RequestMapping(value = "/")
     public String forwardRoot() {
     return "forward:/app.html";
    }
}