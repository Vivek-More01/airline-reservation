package com.airline.airline_reservation_springboot.dto;

import java.util.List;
import java.util.Map;

public class SeatLayoutDTO {
    private int rows;
    private List<String> columns; // e.g., ["A", "B", "C", "AISLE", "D", "E", "F"]
    private Map<String, String> seatTypeMap; // e.g., {"1A": "Business", "10C": "Exit Row"}
    private List<StructureInfoDTO> structures; // Optional list of galleys, lavatories etc.

    // Getters and Setters are crucial for Jackson
    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public Map<String, String> getSeatTypeMap() {
        return seatTypeMap;
    }

    public void setSeatTypeMap(Map<String, String> seatTypeMap) {
        this.seatTypeMap = seatTypeMap;
    }

    public List<StructureInfoDTO> getStructures() {
        return structures;
    }

    public void setStructures(List<StructureInfoDTO> structures) {
        this.structures = structures;
    }

    /**
     * Helper method to get the seat type for a given seat number.
     * Defaults to "Economy" if not found in the map.
     */
    public String getSeatTypeForSeat(String seatNumber) {
        return (seatTypeMap != null) ? seatTypeMap.getOrDefault(seatNumber, "Economy") : "Economy";
    }
}
