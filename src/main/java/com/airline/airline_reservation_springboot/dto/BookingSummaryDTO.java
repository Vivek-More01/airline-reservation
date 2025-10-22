package com.airline.airline_reservation_springboot.dto;

import java.time.LocalDateTime;

/**
 * DTO representing a summary of a booking for the "My Bookings" page.
 */
public class BookingSummaryDTO {
    private int bookingId;
    private String airline;
    private String source;
    private String destination;
    private LocalDateTime departure;
    private String seatNo;
    private String status;
    private String pnr;

    public BookingSummaryDTO(int bookingId, String airline, String source, String destination, LocalDateTime departure,
            String seatNo, String status, String pnr) {
        this.bookingId = bookingId;
        this.airline = airline;
        this.source = source;
        this.destination = destination;
        this.departure = departure;
        this.seatNo = seatNo;
        this.status = status;
        this.pnr = pnr;
    }

    // Getters
    public int getBookingId() {
        return bookingId;
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

    public String getSeatNo() {
        return seatNo;
    }

    public String getStatus() {
        return status;
    }

    public String getPnr() {
        return pnr;
    }

    // Helper method for template logic
    public boolean isCancellable() {
        // Define your cancellation logic here (e.g., only allow cancelling confirmed
        // bookings)
        // Also consider adding a time constraint (e.g., > 24 hours before departure)
        return "CONFIRMED".equalsIgnoreCase(status);
    }
}
