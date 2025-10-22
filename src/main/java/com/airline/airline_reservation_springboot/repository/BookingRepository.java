package com.airline.airline_reservation_springboot.repository;

import com.airline.airline_reservation_springboot.model.Booking;
import com.airline.airline_reservation_springboot.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    // Spring Data JPA will provide all standard CRUD methods (save, findById, etc.)
    List<Booking> findByUserOrderByBookingDateDesc(User user);
}
