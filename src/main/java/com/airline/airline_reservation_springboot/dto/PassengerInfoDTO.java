package com.airline.airline_reservation_springboot.dto;

/**
 * DTO representing information about a single passenger on a flight manifest.
 */
public class PassengerInfoDTO {
    private String name;
    private String email;
    private String seatNo;
    private String pnr;

    public PassengerInfoDTO(String name, String email, String seatNo, String pnr) {
        this.name = name;
        this.email = email;
        this.seatNo = seatNo;
        this.pnr = pnr;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public String getPnr() {
        return pnr;
    }
}
