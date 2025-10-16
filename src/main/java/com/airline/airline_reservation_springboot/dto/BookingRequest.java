package com.airline.airline_reservation_springboot.dto;

public class BookingRequest {

    private int flightId;
    private String seatNumber;

    // Getters and Setters are required for the JSON to be deserialized correctly.
    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}
