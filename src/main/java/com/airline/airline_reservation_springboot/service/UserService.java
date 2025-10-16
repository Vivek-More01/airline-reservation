package com.airline.airline_reservation_springboot.service;

import com.airline.airline_reservation_springboot.model.User;
import com.airline.airline_reservation_springboot.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Spring automatically injects an instance of UserRepository here
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user. Hashes the password before saving.
     * @param user The user object to register.
     * @return The saved user.
     */
    public User registerUser(User user) {
        // Hash the password before saving it to the database
        user.setRole("Passenger"); // Default role
        user.setPassword(user.getPassword());
        
        return userRepository.save(user);
    }

    /**
     * Validates a user's login credentials.
     * @param email The user's email.
     * @param password The user's plain-text password.
     * @return An Optional containing the User if login is successful, otherwise empty.
     */
    public Optional<User> loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Compare the hashed version of the provided password with the stored hash
            if (user.getPasswordHash().equals(hashPassword(password))) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    /**
     * Hashes a password using SHA-256.
     * @param password The plain-text password.
     * @return The hashed password as a hex string.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

