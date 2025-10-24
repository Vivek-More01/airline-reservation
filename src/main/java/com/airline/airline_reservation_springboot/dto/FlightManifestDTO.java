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
    private int seatsTotal; // <-- Added
    private int seatsAvailable; // <-- Added

    public FlightManifestDTO(String airline, String source, String destination, List<PassengerInfoDTO> passengers, int seatsTotal, int seatsAvailable) {
        this.airline = airline;
        this.source = source;
        this.destination = destination;
        this.passengers = passengers;
        this.seatsTotal = seatsTotal;
        this.seatsAvailable = seatsAvailable;
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
    
    public int getSeatsTotal() {
        return seatsTotal;
    } // <-- Added

    public int getSeatsAvailable() {
        return seatsAvailable;
    } // <-- Added

    // Calculated Load Factor
    public double getLoadFactor() {
        if (seatsTotal <= 0) {
            return 0.0;
        }
        return (double) (seatsTotal - seatsAvailable) / seatsTotal;
    }

    // Formatted Load Factor for display
    public String getFormattedLoadFactor() {
        return String.format("%.1f%%", getLoadFactor() * 100);
    }
}
