import React from "react";
import Seat from "./Seat";

const AirplaneLayout = ({ onSelectSeat, selectedSeat, bookedSeats }) => {
  // Defines the visual layout of the A320
  const layout = [];
  const seatLetters = ["A", "B", "C", "D", "E", "F"];

  // Add labels and structure
  layout.push({
    type: "structure",
    label: "--- Galley / Lavatory ---",
    key: "s1",
  });
  const labels = ["", "A", "B", "C", "", "D", "E", "F", ""];
  labels.forEach((label, index) =>
    layout.push({ type: "label", label, key: `l${index}` })
  );

  // Generate seat rows
  for (let i = 1; i <= 25; i++) {
    layout.push({ type: "label", label: i, key: `r${i}l` });
    seatLetters.forEach((letter) => {
      const seatNumber = `${i}${letter}`;
      let specialType = null;
      if (i <= 4) specialType = "premium";
      if (i === 10 || i === 11) specialType = "exit-row";
      if (i === 9 || i === 24 || i === 25) specialType = "limited-recline";

      layout.push({
        number: seatNumber,
        special: specialType,
        key: seatNumber,
      });
    });
    layout.push({ type: "label", label: i, key: `r${i}r` });

    if (i === 9 || i === 15) {
      layout.push({
        type: "structure",
        label: "--- Emergency Exit ---",
        key: `e${i}`,
      });
    }
  }
  layout.push({
    type: "structure",
    label: "--- Lavatory / Galley ---",
    key: "s2",
  });

  return (
    <div className="airplane">
      <div className="airplane-grid">
        {layout.map((item) => {
          if (item.type === "label")
            return (
              <div key={item.key} className="structure">
                {item.label}
              </div>
            );
          if (item.type === "structure")
            return (
              <div key={item.key} className="structure full-width">
                {item.label}
              </div>
            );

          const status = bookedSeats.includes(item.number)
            ? "occupied"
            : "available";
          return (
            <Seat
              key={item.key}
              seatNumber={item.number}
              status={status}
              type={item.special}
              onSelect={onSelectSeat}
              isSelected={selectedSeat === item.number}
            />
          );
        })}
      </div>
    </div>
  );
};

export default AirplaneLayout;
