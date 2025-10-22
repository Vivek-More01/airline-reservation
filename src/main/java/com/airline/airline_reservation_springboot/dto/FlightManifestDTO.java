package com.airline.airline_reservation_springboot.dto;

import java.util.List;

/**
 * DTO representing the complete data needed for the flight manifest page.
 */
public class FlightManifestDTO {
    private String airline;
    private String source;
    private String destination;
    private List<PassengerInfoDTO> passengers;

    public FlightManifestDTO(String airline, String source, String destination, List<PassengerInfoDTO> passengers) {
        this.airline = airline;
        this.source = source;
        this.destination = destination;
        this.passengers = passengers;
    }

    // Getters
    public String getAirline() {
        return airline;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public List<PassengerInfoDTO> getPassengers() {
        return passengers;
    }

    public boolean isPassengerListEmpty() {
        return passengers == null || passengers.isEmpty();
    }
}
