package com.airline.airline_reservation_springboot.dto;
/**
 * A Data Transfer Object representing the publicly-safe information for a user.
 * This prevents lazy loading exceptions and hides sensitive data like
 * passwords.
 */
public class UserDTO {

    private Integer userId;
    private String name;
    private String email;
    private String role;

    // Default constructor (often needed for data binding)
    public UserDTO() {
    }

    // Constructor to easily map from the User entity
    public UserDTO(Integer userId, String name, String email, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // Standard Getters and Setters
    // --- CHANGE: Getter/Setter use Integer ---
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
