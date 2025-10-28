package com.airline.airline_reservation_springboot.dto;

public class StructureInfoDTO {
    private String type; // e.g., "LAVATORY", "GALLEY"
    private String position; // e.g., "FRONT_LEFT", "REAR_CENTER"
    private int row; // Row number where the structure is located

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
