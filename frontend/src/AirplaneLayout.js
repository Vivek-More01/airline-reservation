import React, { useMemo } from "react"; // Import useMemo for performance
import Seat from "./Seat";

// Accept seatLayoutJson as a prop
const AirplaneLayout = ({
  onSelectSeat,
  selectedSeats,
  bookedSeats,
  seatLayoutJson,
}) => {
  // useMemo will parse the JSON only when the seatLayoutJson string changes
  const parsedLayoutData = useMemo(() => {
    if (!seatLayoutJson) {
      console.error("Seat layout JSON is missing.");
      return null; // Handle missing layout data
    }
    try {
      const fullJson = JSON.parse(seatLayoutJson);
      // --- CHANGE: Access data under the 'seatmap' key ---
      return fullJson.seatmap || null; // Return null if 'seatmap' key doesn't exist
    } catch (error) {
      console.error("Failed to parse seat layout JSON:", error);
      return null; // Handle invalid JSON
    }
  }, [seatLayoutJson]); // Dependency array ensures parsing only happens if JSON changes

  // --- Dynamic Layout Generation ---
  const generateLayout = () => {
    // --- CHANGE: Use parsedLayoutData which is now the content of 'seatmap' ---
    if (!parsedLayoutData) {
      return [
        <div key="error" className="structure full-width error">
          Error loading seat layout.
        </div>,
      ];
    }

    const layout = [];
    // --- CHANGE: Destructure directly from parsedLayoutData ---
    const { rows, columns, seatTypeMap, structures } = parsedLayoutData;

    // --- CHANGE: Handle structures based on 'row' property ---
    // Add structures before row 1 (row: 0)
    if (structures) {
      structures
        .filter((s) => s.row === 0)
        .forEach((structure, index) => {
          // You can add more complex rendering based on structure.position (LEFT, RIGHT, CENTER) later
          layout.push({
            type: "structure",
            label: `--- ${structure.type} (${structure.position}) ---`,
            key: `s_front_${index}`,
          });
        });
    }

    // Add column header labels
    columns.forEach((col, index) => {
      layout.push({
        type: "label",
        label: col === "AISLE" ? "" : col,
        key: `h${index}`,
      });
    });

    // Add seat rows
    for (let i = 1; i <= rows; i++) {
      columns.forEach((col) => {
        if (col === "AISLE") {
          // Render aisle space or row number based on position relative to other AISLEs or edges
          const colIndex = columns.indexOf(col);
          const isEdgeOrNearAisle =
            colIndex === 0 ||
            colIndex === columns.length - 1 ||
            (colIndex > 0 && columns[colIndex - 1] === "AISLE") ||
            (colIndex < columns.length - 1 &&
              columns[colIndex + 1] === "AISLE");
          if (isEdgeOrNearAisle) {
            layout.push({
              type: "label",
              label: i,
              key: `r${i}${col}${colIndex}`,
            }); // Use colIndex for unique key
          } else {
            layout.push({
              type: "aisle",
              label: "",
              key: `r${i}${col}${colIndex}`,
            });
          }
        } else {
          const seatNumber = `${i}${col}`;
          // Determine seat type from map, default to Economy
          // --- CHANGE: Access seatTypeMap from parsedLayoutData ---
          const seatType = seatTypeMap?.[seatNumber] || "Economy";
          layout.push({ number: seatNumber, type: seatType, key: seatNumber });
        }
      });

      // --- CHANGE: Add structures located *after* this specific row ---
      if (structures) {
        structures
          .filter((s) => s.row === i)
          .forEach((structure, index) => {
            layout.push({
              type: "structure",
              label: `--- ${structure.type} (${structure.position}) ---`,
              key: `s_after_${i}_${index}`,
            });
          });
      }
    }

    // Note: Structures defined with row > total rows might not appear depending on exact logic.
    // We handle row 0 before headers and row 'i' after row 'i' seats.

    return layout;
  };

  const gridLayout = generateLayout();
  // --- CHANGE: Calculate gridCols from parsedLayoutData ---
  const gridCols = parsedLayoutData ? parsedLayoutData.columns.length : 1;

  return (
    <div className="airplane">
      {/* Apply dynamic grid columns */}
      <div
        className="airplane-grid"
        style={{ gridTemplateColumns: `repeat(${gridCols}, minmax(0, 1fr))` }}
      >
        {gridLayout.map((item) => {
          // Render logic based on item type (label, aisle, structure, seat)
          if (item.type === "aisle")
            return (
              <div key={item.key} className="aisle">
                {item.label}
              </div>
            );
          if (item.type === "label")
            return (
              <div key={item.key} className="structure">
                {item.label}
              </div>
            );
          // Ensure structures span full width
          if (item.type === "structure")
            return (
              <div
                key={item.key}
                className="structure full-width"
                style={{ gridColumn: `1 / span ${gridCols}` }}
              >
                {item.label}
              </div>
            );

          // It's a seat
          const status = bookedSeats.includes(item.number)
            ? "occupied"
            : "available";
          const isSelected = selectedSeats.includes(item.number);

          return (
            <Seat
              key={item.key}
              seatNumber={item.number}
              status={status}
              type={item.type} // Pass the type determined from JSON
              onSelect={onSelectSeat}
              isSelected={isSelected}
            />
          );
        })}
      </div>
    </div>
  );
};

export default AirplaneLayout;
