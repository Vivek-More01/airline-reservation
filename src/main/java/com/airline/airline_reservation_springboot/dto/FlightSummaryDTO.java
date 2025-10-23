package com.airline.airline_reservation_springboot.dto;

import java.time.LocalDateTime;

/**
 * DTO representing a simplified summary of a flight for display in lists (like
 * the staff dashboard).
 */
public class FlightSummaryDTO {
    private int flightId;
    private String airline;
    private String source;
    private String destination;
    private LocalDateTime departure;
    private int seatsAvailable;
    private int seatsTotal;
    private String status;


    // Constructor to map from the Flight entity
    public FlightSummaryDTO(int flightId, String airline, String source, String destination, LocalDateTime departure,
            int seatsAvailable, int seatsTotal, String status) {
        this.flightId = flightId;
        this.airline = airline;
        this.source = source;
        this.destination = destination;
        this.departure = departure;
        this.seatsAvailable = seatsAvailable;
        this.seatsTotal = seatsTotal;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    // Getters
    public int getFlightId() {
        return flightId;
    }

    public String getAirline() {
        return airline;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public int getSeatsTotal() {
        return seatsTotal;
    }

    // Calculated property for occupancy
    public int getOccupancy() {
        return seatsTotal - seatsAvailable;
    }
}
