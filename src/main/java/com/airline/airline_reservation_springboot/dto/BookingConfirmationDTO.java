package com.airline.airline_reservation_springboot.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.airline.airline_reservation_springboot.model.Booking;
import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.model.User;

public class BookingConfirmationDTO {

    private String pnr;
    private String seatNo;
    private String seatTypeName; // Added
    private BigDecimal calculatedPrice; // Added
    private LocalDateTime bookingDate;
    private String passengerName;
    private String airlineName; // Changed
    private String source;
    private String destination;
    private LocalDateTime departure; // Added departure time
    private LocalDateTime arrival; // Added arrival time

    // Constructor updated to map from Booking entity (requires eager fetching in
    // service)
    public BookingConfirmationDTO(Booking booking) {
        User user = booking.getUser(); // Assumes user is eagerly fetched or available in session
        Flight flight = booking.getFlight(); // Assumes flight is eagerly fetched or available in session

        this.pnr = booking.getPnr();
        this.seatNo = booking.getSeatNo();
        this.seatTypeName = (booking.getSeatType() != null) ? booking.getSeatType().getTypeName() : "N/A"; // Added
        this.calculatedPrice = booking.getCalculatedPrice(); // Added
        this.bookingDate = booking.getBookingDate();
        this.passengerName = (user != null) ? user.getName() : "N/A";
        this.airlineName = (flight != null && flight.getAirline() != null) ? flight.getAirline().getAirlineName()
                : "N/A"; // Changed
        this.source = (flight != null) ? flight.getSource() : "N/A";
        this.destination = (flight != null) ? flight.getDestination() : "N/A";
        this.departure = (flight != null) ? flight.getDeparture() : null; // Added
        this.arrival = (flight != null) ? flight.getArrival() : null; // Added
    }

    // Getters
    public String getPnr() {
        return pnr;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public String getSeatTypeName() {
        return seatTypeName;
    } // Added

    public BigDecimal getCalculatedPrice() {
        return calculatedPrice;
    } // Added

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getAirlineName() {
        return airlineName;
    } // Changed

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDeparture() {
        return departure;
    } // Added

    public LocalDateTime getArrival() {
        return arrival;
    } // Added

    // Formatted price for display
    public String getFormattedCalculatedPrice() {
        return (calculatedPrice != null) ? String.format("$%,.2f", calculatedPrice) : "$N/A";
    }
}