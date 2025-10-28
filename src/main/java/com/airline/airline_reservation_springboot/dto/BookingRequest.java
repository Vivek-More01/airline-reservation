package com.airline.airline_reservation_springboot.dto;

import com.airline.airline_reservation_springboot.model.Passenger;

public class BookingRequest {

    private int flightId;
    private String seatNumber;
    private Passenger passengerDetails;

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

    public Passenger getPassengerDetails() {
        return passengerDetails;
    }

    public void setPassengerDetails(Passenger passengerDetails) {
        this.passengerDetails = passengerDetails;
    }
}
