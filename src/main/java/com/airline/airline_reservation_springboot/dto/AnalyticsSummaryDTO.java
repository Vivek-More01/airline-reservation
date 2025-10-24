package com.airline.airline_reservation_springboot.dto;

import java.math.BigDecimal;

/**
 * DTO to hold key performance indicators for the admin analytics dashboard.
 */
public class AnalyticsSummaryDTO {

    private long totalActiveFlights;
    private long totalConfirmedBookings;
    private BigDecimal totalRevenue; // Using BigDecimal for currency
    private double averageLoadFactor; // Percentage (0.0 to 1.0)

    // Constructor
    public AnalyticsSummaryDTO(long totalActiveFlights, long totalConfirmedBookings, BigDecimal totalRevenue,
            double averageLoadFactor) {
        this.totalActiveFlights = totalActiveFlights;
        this.totalConfirmedBookings = totalConfirmedBookings;
        this.totalRevenue = (totalRevenue != null) ? totalRevenue : BigDecimal.ZERO; // Handle null sum
        this.averageLoadFactor = averageLoadFactor;
    }

    // Getters
    public long getTotalActiveFlights() {
        return totalActiveFlights;
    }

    public long getTotalConfirmedBookings() {
        return totalConfirmedBookings;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public double getAverageLoadFactor() {
        return averageLoadFactor;
    }

    // Formatted getter for display
    public String getFormattedAverageLoadFactor() {
        return String.format("%.1f%%", averageLoadFactor * 100);
    }

    public String getFormattedTotalRevenue() {
        return String.format("$%,.2f", totalRevenue);
    }
}
