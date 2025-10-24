package com.airline.airline_reservation_springboot.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "seat_types")
public class SeatType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_type_id")
    private Integer seatTypeId;

    @Column(name = "type_name", nullable = false, unique = true)
    private String typeName;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Relationship: A seat type can be associated with many pricing rules
    @OneToMany(mappedBy = "seatType", fetch = FetchType.LAZY)
    private List<PricingRule> pricingRules;

    // Relationship: A seat type can be assigned to many bookings
    @OneToMany(mappedBy = "seatType", fetch = FetchType.LAZY)
    private List<Booking> bookings;

    // Getters and Setters
    public Integer getSeatTypeId() {
        return seatTypeId;
    }

    public void setSeatTypeId(Integer seatTypeId) {
        this.seatTypeId = seatTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PricingRule> getPricingRules() {
        return pricingRules;
    }

    public void setPricingRules(List<PricingRule> pricingRules) {
        this.pricingRules = pricingRules;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
