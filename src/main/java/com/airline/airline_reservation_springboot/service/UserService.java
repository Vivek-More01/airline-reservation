package com.airline.airline_reservation_springboot.service;

import com.airline.airline_reservation_springboot.dto.UserDTO;
import com.airline.airline_reservation_springboot.model.User;
import com.airline.airline_reservation_springboot.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
// import java.nio.charset.StandardCharsets;
// import java.security.MessageDigest;
// import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    // Update the constructor
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user. Hashes the password before saving.
     * @param user The user object to register.
     * @return The saved user.
     */
    public User registerUser(User user) {
        // Hash the password before saving it to the database
        user.setRole("Passenger"); // Default role
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // user.setPasswordHash(passwordEncoder.encode(user.getPassword()));;
        
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
            if (user.getPassword().equals(passwordEncoder.encode(password))) {
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
    // private String hashPassword(String password) {
    //     try {
    //         MessageDigest digest = MessageDigest.getInstance("SHA-256");
    //         byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
    //         return bytesToHex(encodedhash);
    //     } catch (NoSuchAlgorithmException e) {
    //         throw new RuntimeException("Error hashing password", e);
    //     }
    // }

    // private String bytesToHex(byte[] hash) {
    //     StringBuilder hexString = new StringBuilder(2 * hash.length);
    //     for (byte b : hash) {
    //         String hex = Integer.toHexString(0xff & b);
    //         if (hex.length() == 1) {
    //             hexString.append('0');
    //         }
    //         hexString.append(hex);
    //     }
    //     return hexString.toString();
    // }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // --- NEW METHOD ---
    /**
     * Saves changes to a user entity. Use this for profile updates.
     * Assumes password hashing is handled elsewhere if needed (like in
     * changePassword).
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    public UserDTO convertToDTO(User user) {
        return new UserDTO(user.getUserId(), user.getName(), user.getEmail(), user.getRole());
    }

    // --- NEW METHODS FOR ADMIN USER MANAGEMENT ---

    /** Finds all users with a specific role. */
    public List<User> findUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    /** Finds all ACTIVE users with a specific role. */
    public List<User> findActiveUsersByRole(String role) {
        return userRepository.findByRoleAndAccountStatus(role, "ACTIVE");
    }

    /** Finds a user by ID. */
    public Optional<User> findById(Integer userId) {
        return userRepository.findById(userId);
    }

    /** Sets a user's status to BANNED. */
    @Transactional
    public boolean banUser(Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Optional: Prevent banning Admins or self-banning? Add checks here.
            user.setAccountStatus("BANNED");
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /** Sets a user's status to ACTIVE. */
    @Transactional
    public boolean unbanUser(Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setAccountStatus("ACTIVE");
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * "Deletes" a user by setting their status to INACTIVE.
     * We avoid hard deletes to preserve booking history integrity.
     */
    @Transactional
    public boolean deleteUser(Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Optional: Add checks (e.g., cannot delete Admins).
            user.setAccountStatus("INACTIVE"); // Or a dedicated 'DELETED' status
            userRepository.save(user);
            return true;
        }
        return false;
    }
}

