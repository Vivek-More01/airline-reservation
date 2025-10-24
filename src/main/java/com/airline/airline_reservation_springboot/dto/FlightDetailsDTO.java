package com.airline.airline_reservation_springboot.dto;

import com.airline.airline_reservation_springboot.model.Flight;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FlightDetailsDTO {

    private int flightId;
    private String airlineName; // Changed
    private String aircraftModel; // Added
    private String source;
    private String destination;
    private LocalDateTime departure;
    private LocalDateTime arrival;
    private BigDecimal basePrice; // Changed
    private int seatsTotal; // Added (from Aircraft)
    private String seatLayoutJson; // Added
    private List<String> bookedSeats; // (Confirmed/CheckedIn seats)

    // Constructor updated to map from Flight entity
    public FlightDetailsDTO(Flight flight, List<String> bookedSeats) {
        this.flightId = flight.getFlightId();
        // Safely get related data
        this.airlineName = (flight.getAirline() != null) ? flight.getAirline().getAirlineName() : "N/A"; // Changed
        this.aircraftModel = (flight.getAircraft() != null) ? flight.getAircraft().getAircraftModel() : "N/A"; // Added
        this.source = flight.getSource();
        this.destination = flight.getDestination();
        this.departure = flight.getDeparture();
        this.arrival = flight.getArrival();
        this.basePrice = flight.getBasePrice(); // Changed
        this.seatsTotal = flight.getTotalSeats(); // Added (uses helper)
        this.seatLayoutJson = (flight.getAircraft() != null) ? flight.getAircraft().getSeatLayoutJson() : null; // Added
        this.bookedSeats = bookedSeats;
    }

    // Getters
    public int getFlightId() {
        return flightId;
    }

    public String getAirlineName() {
        return airlineName;
    } // Changed

    public String getAircraftModel() {
        return aircraftModel;
    } // Added

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

    public BigDecimal getBasePrice() {
        return basePrice;
    } // Changed

    public int getSeatsTotal() {
        return seatsTotal;
    } // Added

    public String getSeatLayoutJson() {
        return seatLayoutJson;
    } // Added

    public List<String> getBookedSeats() {
        return bookedSeats;
    }

    // Formatted price for display
    public String getFormattedBasePrice() {
        return (basePrice != null) ? String.format("$%,.2f", basePrice) : "$N/A";
    }
}