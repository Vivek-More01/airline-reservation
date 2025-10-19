import React from "react";
import Seat from "./Seat";

const AirplaneLayout = ({ onSelectSeat, selectedSeat, bookedSeats }) => {
  // Defines the visual layout of the A320
  const layout = [];
  // const seatLetters = ["A", "B", "C", "D", "E", "F"];

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
  // let col = 0;
  // Generate seat rows
  for (let i = 1; i <= 25; i++) {
    let j = 1;
    labels.forEach((x) => {
      if (x === ""){
        layout.push({ type: "label", label: "", key: `r${j}${i}l}` });
        j++;
        return
      }
      const seatNumber = `${i}${x}`;
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
  console.log(layout);
  return (
    <div className="airplane">
      <div className="airplane-grid">
        {layout.map((item) => {
          if (item.type === "label"){  
            if (item.label === " ") {
              return (
                <div key={item.key} className="label empty-label">
                  &nbsp;
                </div>
              );
            } else {
              return (
                <div key={item.key} className="label">
                  {item.label}
                </div>
              );
            }
          }
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
