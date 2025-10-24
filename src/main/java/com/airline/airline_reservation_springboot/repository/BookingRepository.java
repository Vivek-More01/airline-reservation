package com.airline.airline_reservation_springboot.repository;

import com.airline.airline_reservation_springboot.model.Booking;
import com.airline.airline_reservation_springboot.model.User;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    // Spring Data JPA will provide all standard CRUD methods (save, findById, etc.)
    List<Booking> findByUserOrderByBookingDateDesc(User user);

    /** Counts bookings with a specific status. */
    long countByStatusIgnoreCase(String status);

    /** Calculates the total revenue from confirmed bookings. */
    @Query("SELECT SUM(b.flight.price) FROM Booking b WHERE b.status = 'CONFIRMED'")
    BigDecimal calculateTotalRevenue();
}
