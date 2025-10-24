package com.airline.airline_reservation_springboot.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "aircraft")
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aircraft_id")
    private Integer aircraftId;

    @Column(name = "aircraft_model", nullable = false, unique = true)
    private String aircraftModel;

    @Column(name = "total_seats", nullable = false)
    private int totalSeats;

    // Map JSON column as a String for now. Parsing handled in service layer.
    @Lob // Use @Lob for potentially large JSON strings
    @Column(name = "seat_layout_json", columnDefinition = "JSON") // Specify column type if DB supports it
    private String seatLayoutJson;

    // Relationship: An aircraft type can be used for many flights
    @OneToMany(mappedBy = "aircraft", fetch = FetchType.LAZY)
    private List<Flight> flights;

    // Getters and Setters
    public Integer getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(Integer aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getAircraftModel() {
        return aircraftModel;
    }

    public void setAircraftModel(String aircraftModel) {
        this.aircraftModel = aircraftModel;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public String getSeatLayoutJson() {
        return seatLayoutJson;
    }

    public void setSeatLayoutJson(String seatLayoutJson) {
        this.seatLayoutJson = seatLayoutJson;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
