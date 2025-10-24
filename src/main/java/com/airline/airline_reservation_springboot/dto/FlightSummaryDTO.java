package com.airline.airline_reservation_springboot.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.airline.airline_reservation_springboot.model.Flight;

/**
 * DTO representing a simplified summary of a flight for display in lists (like
 * the staff dashboard).
 */
public class FlightSummaryDTO {
    private int flightId;
    private String airlineName; // Changed from airline
    private String source;
    private String destination;
    private LocalDateTime departure;
    private LocalDateTime arrival;
    private int seatsAvailable;
    private int seatsTotal; // Now comes from Aircraft
    private String status;
    private BigDecimal basePrice; // Changed from price

    // Constructor to map from the Flight entity
    public FlightSummaryDTO(Flight flight) {
        this.flightId = flight.getFlightId();
        // Safely get airline name
        this.airlineName = (flight.getAirline() != null) ? flight.getAirline().getAirlineName() : "N/A";
        this.source = flight.getSource();
        this.destination = flight.getDestination();
        this.departure = flight.getDeparture();
        this.seatsAvailable = flight.getSeatsAvailable();
        // Safely get total seats from aircraft
        this.seatsTotal = flight.getTotalSeats(); // Use helper method
        this.status = flight.getStatus();
        this.basePrice = flight.getBasePrice();
        this.arrival = flight.getArrival();
    }

    // Getters
    public int getFlightId() {
        return flightId;
    }

    public String getAirlineName() {
        return airlineName;
    } // Changed getter name

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }
    public LocalDateTime getArrival() {
        return arrival;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public int getSeatsTotal() {
        return seatsTotal;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    } // Changed getter name

    // Calculated property for occupancy
    public int getOccupancy() {
        return (seatsTotal > 0) ? seatsTotal - seatsAvailable : 0;
    }

    // Formatted price for display
    public String getFormattedBasePrice() {
        return (basePrice != null) ? String.format("$%,.2f", basePrice) : "$N/A";
    }
}