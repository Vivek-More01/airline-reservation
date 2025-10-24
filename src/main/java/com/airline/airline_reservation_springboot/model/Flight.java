package com.airline.airline_reservation_springboot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flight")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private int flightId;

    // --- UPDATED: Relationship to Airline ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    // --- NEW: Relationship to Aircraft ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_id", nullable = false)
    private Aircraft aircraft;

    // @Column(name = "airline")

    // private String airline;

    @Column(name = "source")
    private String source;

    @Column(name = "destination")
    private String destination;

    @Column(name = "departure")
    private LocalDateTime departure;

    @Column(name = "arrival")
    private LocalDateTime arrival;
    
    @Column(name = "status")
    private String status;

    // @Column(name = "seats_total")
    // private int seatsTotal;

    @Column(name = "seats_available")
    private int seatsAvailable;

    // --- UPDATED: Renamed to basePrice ---
    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice; // Base price (e.g., for Economy)

    // This is the new relationship mapping
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // Manages the forward part of the relationship for JSON
    private List<Booking> bookings = new ArrayList<>();

    // --- Getters and Setters ---
    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public Airline getAirline() {
        return airline;
    } // Updated getter type

    public void setAirline(Airline airline) {
        this.airline = airline;
    } // Updated setter type

    public Aircraft getAircraft() {
        return aircraft;
    } // New getter

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    } // New setter

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

    // public int getSeatsTotal() { return seatsTotal; } // Removed
    // public void setSeatsTotal(int seatsTotal) { this.seatsTotal = seatsTotal; }
    // // Removed
    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    } // Updated getter

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    } // Updated setter

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    // Helper to get total seats from aircraft (lazy loading handled in service if
    // needed)
    public int getTotalSeats() {
        return (this.aircraft != null) ? this.aircraft.getTotalSeats() : 0;
    }
}
