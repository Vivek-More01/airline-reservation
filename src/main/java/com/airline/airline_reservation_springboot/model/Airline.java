package com.airline.airline_reservation_springboot.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "airlines")
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airline_id")
    private Integer airlineId;

    @Column(name = "airline_name", nullable = false, unique = true)
    private String airlineName;

    @Column(name = "iata_code", unique = true, length = 3)
    private String iataCode;

    // Relationship: An airline can have many flights
    @OneToMany(mappedBy = "airline", fetch = FetchType.LAZY)
    private List<Flight> flights;

    // Relationship: An airline can have specific pricing rules
    @OneToMany(mappedBy = "airline", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PricingRule> pricingRules;

    // Getters and Setters
    public Integer getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(Integer airlineId) {
        this.airlineId = airlineId;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public List<PricingRule> getPricingRules() {
        return pricingRules;
    }

    public void setPricingRules(List<PricingRule> pricingRules) {
        this.pricingRules = pricingRules;
    }
}
