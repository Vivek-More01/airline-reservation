package com.airline.airline_reservation_springboot.dto;

import java.time.LocalDateTime;

/**
 * A Data Transfer Object to hold all necessary information for the booking
 * confirmation page.
 * This object is "flat" and has no lazy-loaded relationships, preventing
 * view-rendering errors.
 */
public class BookingConfirmationDTO {

    // Booking Details
    private String pnr;
    private String seatNo;
    private LocalDateTime bookingDate;

    // User Details
    private String passengerName;

    // Flight Details
    private String airline;
    private String source;
    private String destination;

    // Constructor to make mapping easy
    public BookingConfirmationDTO(String pnr, String seatNo, LocalDateTime bookingDate, String passengerName,
            String airline, String source, String destination) {
        this.pnr = pnr;
        this.seatNo = seatNo;
        this.bookingDate = bookingDate;
        this.passengerName = passengerName;
        this.airline = airline;
        this.source = source;
        this.destination = destination;
    }

    // Standard Getters
    public String getPnr() {
        return pnr;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public String getPassengerName() {
        return passengerName;
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
}
