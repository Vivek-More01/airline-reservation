package com.airline.airline_reservation_springboot.dto;

import com.airline.airline_reservation_springboot.model.Passenger; // Import Passenger model
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO used as a wrapper for submitting details of multiple passengers from a
 * form.
 */
public class PassengerDTO {

    private int flightId;
    private List<String> seatNumbers = new ArrayList<>(); // The seats being booked
    private List<Passenger> passengers = new ArrayList<>(); // Passenger details corresponding to each seat

    // Getters and Setters
    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public List<String> getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(List<String> seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    // Helper to ensure the passengers list matches the number of seats
    public void ensurePassengerListSize() {
        int requiredSize = (seatNumbers != null) ? seatNumbers.size() : 0;
        while (passengers.size() < requiredSize) {
            passengers.add(new Passenger());
        }
        while (passengers.size() > requiredSize) {
            passengers.remove(passengers.size() - 1);
        }
    }
}
