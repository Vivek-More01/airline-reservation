import React from "react";

const Seat = ({ seatNumber, status, type, onSelect, isSelected }) => {
  const getSeatClass = () => {
    let classes = "seat ";
    if (isSelected) return classes + "selected";
    if (status === "occupied") return classes + "occupied";
    if (status === "available") classes += "available ";

    if (type === "premium") classes += "premium ";
    if (type === "exit-row") classes += "exit-row ";
    if (type === "limited-recline") classes += "limited-recline ";
    return classes.trim();
  };

  const handleClick = () => {
    if (status === "available") {
      onSelect(seatNumber);
    }
  };
  return (
    <div className={getSeatClass()} onClick={handleClick}>
      {seatNumber}
    </div>
  );
};

export default Seat;
