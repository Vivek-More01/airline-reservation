package com.airline.airline_reservation_springboot.dto;

/**
 * DTO representing information about a single passenger on a flight manifest.
 */
public class PassengerInfoDTO {
    private String name;
    private String email;
    private String seatNo;
    private String pnr;
    private int bookingId;
    private String status;

    // Constructor

    public PassengerInfoDTO(String name, String email, String seatNo, String pnr, int bookingId, String status) {
        this.name = name;
        this.email = email;
        this.seatNo = seatNo;
        this.pnr = pnr;
        this.bookingId = bookingId;
        this.status = status;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public String getPnr() {
        return pnr;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getStatus() {
        return status;
    }
}
