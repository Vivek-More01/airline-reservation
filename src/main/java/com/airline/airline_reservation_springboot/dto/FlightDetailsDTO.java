package com.airline.airline_reservation_springboot.dto;

import com.airline.airline_reservation_springboot.model.Flight;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FlightDetailsDTO {

    private int flightId;
    private String airline;
    private String source;
    private String destination;
    private LocalDateTime departure;
    private LocalDateTime arrival;
    private BigDecimal price;
    private List<String> bookedSeats;

    public FlightDetailsDTO(Flight flight, List<String> bookedSeats) {
        this.flightId = flight.getFlightId();
        this.airline = flight.getAirline();
        this.source = flight.getSource();
        this.destination = flight.getDestination();
        this.departure = flight.getDeparture();
        this.arrival = flight.getArrival();
        this.price = flight.getPrice();
        this.bookedSeats = bookedSeats;
    }

    // --- Getters and Setters ---

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDateTime departure) {
        this.departure = departure;
    }

    public LocalDateTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalDateTime arrival) {
        this.arrival = arrival;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<String> getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(List<String> bookedSeats) {
        this.bookedSeats = bookedSeats;
    }
}
