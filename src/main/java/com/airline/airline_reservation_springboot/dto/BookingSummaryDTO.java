package com.airline.airline_reservation_springboot.dto;

import com.airline.airline_reservation_springboot.model.Booking; // Import Booking
import java.math.BigDecimal; // Import BigDecimal
import java.time.LocalDateTime;

/**
 * DTO representing a summary of a booking for the "My Bookings" page.
 */
public class BookingSummaryDTO {
    private int bookingId;
    private String airlineName; // Changed from airline
    private String source;
    private String destination;
    private LocalDateTime departure;
    private String seatNo;
    private String seatTypeName; // Added
    private BigDecimal calculatedPrice; // Added
    private String status;
    private String pnr;

    // Constructor updated to map from Booking entity
    public BookingSummaryDTO(Booking booking) {
        this.bookingId = booking.getBookingId();
        // Safely get related data
        this.airlineName = (booking.getFlight() != null && booking.getFlight().getAirline() != null)
                ? booking.getFlight().getAirline().getAirlineName()
                : "N/A";
        this.source = (booking.getFlight() != null) ? booking.getFlight().getSource() : "N/A";
        this.destination = (booking.getFlight() != null) ? booking.getFlight().getDestination() : "N/A";
        this.departure = (booking.getFlight() != null) ? booking.getFlight().getDeparture() : null;
        this.seatNo = booking.getSeatNo();
        this.seatTypeName = (booking.getSeatType() != null) ? booking.getSeatType().getTypeName() : "N/A"; // Added
        this.calculatedPrice = booking.getCalculatedPrice(); // Added
        this.status = booking.getStatus();
        this.pnr = booking.getPnr();
    }

    // Getters
    public int getBookingId() {
        return bookingId;
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

    public String getStatus() {
        return status;
    }

    public String getPnr() {
        return pnr;
    }

    public boolean isCancellable() {
        // Keep existing logic or refine based on status/time
        return "CONFIRMED".equalsIgnoreCase(status);
    }

    // Formatted price for display
    public String getFormattedCalculatedPrice() {
        return (calculatedPrice != null) ? String.format("$%,.2f", calculatedPrice) : "$N/A";
    }
}
