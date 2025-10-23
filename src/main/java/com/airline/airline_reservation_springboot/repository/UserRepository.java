package com.airline.airline_reservation_springboot.repository;

import com.airline.airline_reservation_springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Spring Data JPA will automatically generate the implementation for this method
     * based on its name. It will create a query that finds a User by their email.
     *
     * @param email The email of the user to find.
     * @return An Optional containing the User if found, otherwise empty.
     */
    Optional<User> findByEmail(String email);
    
    List<User> findByRole(String role);

    List<User> findByRoleAndAccountStatus(String role, String accountStatus); // To find active staff/passengers

}
