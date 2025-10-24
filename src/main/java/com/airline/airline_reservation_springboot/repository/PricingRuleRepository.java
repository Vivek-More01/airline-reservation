package com.airline.airline_reservation_springboot.repository;

import com.airline.airline_reservation_springboot.model.Airline; // Import Airline
import com.airline.airline_reservation_springboot.model.PricingRule;
import com.airline.airline_reservation_springboot.model.SeatType; // Import SeatType
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Import Query
import org.springframework.data.repository.query.Param; // Import Param

import java.util.Optional;

public interface PricingRuleRepository extends JpaRepository<PricingRule, Integer> {

    // Find the most specific rule: airline-specific first, then general
    @Query("SELECT pr FROM PricingRule pr WHERE pr.seatType = :seatType " +
            "AND (pr.airline = :airline OR pr.airline IS NULL) " +
            "ORDER BY pr.airline DESC") // Prioritize non-null airline (airline-specific rule)
    Optional<PricingRule> findBestMatch(@Param("airline") Airline airline, @Param("seatType") SeatType seatType);

    // Find a general rule (airline is null) for a specific seat type
    Optional<PricingRule> findByAirlineIsNullAndSeatType(SeatType seatType);

    // Find an airline-specific rule
    Optional<PricingRule> findByAirlineAndSeatType(Airline airline, SeatType seatType);
}