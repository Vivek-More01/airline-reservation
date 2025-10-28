package com.airline.airline_reservation_springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal; // Import BigDecimal
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId; // Use Integer

    @Column(name = "seat_no", nullable = false)
    private String seatNo;

    // --- NEW: Relationship to SeatType ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_type_id", nullable = false)
    private SeatType seatType;

    // --- NEW: Price paid at time of booking ---
    @Column(name = "calculated_price", nullable = false)
    private BigDecimal calculatedPrice;

    @Column(nullable = false)
    private String status;

    @Column(name = "booking_date", nullable = false)
    private LocalDateTime bookingDate;

    @Column(unique = true, nullable = false)
    private String pnr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    @JsonBackReference("flight-booking")
    private Flight flight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // Keep this for User-Booking relationship
    private User user;

    // --- NEW: Relationship to Passenger ---
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }) // Cascade needed if saving new Passenger with Booking
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger; // The person travelling

    // --- Getters and Setters ---
    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public SeatType getSeatType() {
        return seatType;
    } // New Getter

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    } // New Setter

    public BigDecimal getCalculatedPrice() {
        return calculatedPrice;
    } // New Getter

    public void setCalculatedPrice(BigDecimal calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    } // New Setter

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Passenger getPassenger() {
        return passenger;
    } // New Getter
    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    } // New Setter
}