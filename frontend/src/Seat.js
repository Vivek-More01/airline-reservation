import React from "react";

// isSelected is now passed down from AirplaneLayout
const Seat = ({ seatNumber, status, type, onSelect, isSelected }) => {
  const getSeatClass = () => {
    let classes = "seat ";
    // --- CHANGE: Check isSelected prop first ---
    if (isSelected) return classes + "selected";
    if (status === "occupied") return classes + "occupied";
    if (status === "available") classes += "available ";

    // Add type styles (premium, exit-row, etc.)
    if (type === "Business") classes += "premium "; // Example mapping
    if (type === "Exit Row") classes += "exit-row ";
    if (type === "Limited Recline") classes += "limited-recline ";

    return classes.trim();
  };

  const handleClick = () => {
    // Only allow selecting/deselecting available seats
    if (status === "available") {
      onSelect(seatNumber); // Call the handler passed from App.js via AirplaneLayout
    }
  };

  return (
    <div className={getSeatClass()} onClick={handleClick}>
      {seatNumber}
    </div>
  );
};

export default Seat;
