package com.airline.airline_reservation_springboot.service;

import com.airline.airline_reservation_springboot.model.Flight;
import com.airline.airline_reservation_springboot.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional; // <-- This import is required

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> searchFlights(String source, String destination, LocalDate departureDate) {
        return flightRepository.findBySourceAndDestinationAndDepartureDate(source, destination, departureDate);
    }

    public Optional<Flight> findById(int flightId) {
        return flightRepository.findById(flightId);
    }
}

