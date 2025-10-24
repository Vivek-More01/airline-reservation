package com.airline.airline_reservation_springboot.service;
import com.airline.airline_reservation_springboot.dto.AnalyticsSummaryDTO;
import com.airline.airline_reservation_springboot.repository.BookingRepository;
import com.airline.airline_reservation_springboot.repository.FlightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AnalyticsService {

    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    public AnalyticsService(FlightRepository flightRepository, BookingRepository bookingRepository) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Calculates the overall system analytics summary.
     * 
     * @return An AnalyticsSummaryDTO containing key metrics.
     */
    @Transactional(readOnly = true) // Good practice for read-only operations spanning multiple queries
    public AnalyticsSummaryDTO getAnalyticsSummary() {
        long activeFlights = flightRepository.countByStatusNotIgnoreCase("Cancelled");
        long confirmedBookings = bookingRepository.countByStatusIgnoreCase("CONFIRMED");
        BigDecimal totalRevenue = bookingRepository.calculateTotalRevenue();
        Double avgLoadFactor = flightRepository.calculateAverageLoadFactor();

        // Handle potential null from AVG if there are no flights
        double loadFactor = (avgLoadFactor != null) ? avgLoadFactor : 0.0;

        return new AnalyticsSummaryDTO(activeFlights, confirmedBookings, totalRevenue, loadFactor);
    }
}
