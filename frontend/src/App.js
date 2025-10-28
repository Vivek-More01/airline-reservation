import React, { useState, useEffect } from "react";
import AirplaneLayout from "./AirplaneLayout";
import "./App.css";

function App() {
  const [currentUser, setCurrentUser] = useState(null);
  const [flightDetails, setFlightDetails] = useState(null);
  // --- CHANGE: State for multiple seats ---
  const [selectedSeats, setSelectedSeats] = useState([]); // Now an array
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // --- Fetch user session ---
    fetch("http://localhost:8080/api/user/me", { credentials: "include" })
      .then((response) => (response.ok ? response.json() : null))
      .then((user) => setCurrentUser(user))
      .catch((err) => console.error("Error fetching user session:", err));

    // --- Fetch flight details ---
    const params = new URLSearchParams(window.location.search);
    const flightId = params.get("flightId");
    if (!flightId) {
      setError("No flight ID provided in the URL.");
      setLoading(false);
      return;
    }
    fetch(`http://localhost:8080/api/flights/${flightId}`, {
      credentials: "include",
    })
      .then((response) => {
        if (!response.ok) throw new Error("Flight not found or API error.");
        return response.json();
      })
      .then((data) => {
        setFlightDetails(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  // --- CHANGE: Seat selection/deselection logic ---
  const handleSeatSelect = (seatNumber) => {
    setSelectedSeats((prevSeats) => {
      if (prevSeats.includes(seatNumber)) {
        // If already selected, remove it (deselect)
        return prevSeats.filter((seat) => seat !== seatNumber);
      } else {
        // If not selected, add it
        return [...prevSeats, seatNumber];
      }
    });
  };

  // --- CHANGE: Handle proceeding to passenger details ---
  const handleProceed = () => {
    if (!currentUser) {
      alert("Session expired. Please log in again to book flights.");
      window.location.href = `http://localhost:8080/login?returnUrl=${encodeURIComponent(
        window.location.href
      )}`;
      return;
    }
    if (selectedSeats.length === 0) {
      alert("Please select at least one seat.");
      return;
    }

    // Build the URL for the passenger details form
    const params = new URLSearchParams();
    params.append("flightId", flightDetails.flightId);
    selectedSeats.forEach((seat) => params.append("seatNumbers", seat));

    // Redirect to the Spring Boot Thymeleaf page
    window.location.href = `http://localhost:8080/booking/passenger-details?${params.toString()}`;
  };

  if (loading) return <div className="status-message">Loading...</div>;
  if (error) return <div className="status-message error">{error}</div>;
  if (!flightDetails)
    return <div className="status-message">Flight details not available.</div>; // Added check

  return (
    <div className="container">
      {currentUser && (
        <div className="welcome-banner">Welcome, {currentUser.name}!</div>
      )}

      <div className="card">
        <div className="header">
          <h1>Select Your Seat(s)</h1> {/* Updated Title */}
          <div className="flight-info">
            <p>
              {flightDetails.airlineName}: {flightDetails.source} to{" "}
              {flightDetails.destination}
            </p>{" "}
            {/* Use DTO field */}
            <p className="price">
              Base Price: $
              {flightDetails.basePrice
                ? flightDetails.basePrice.toFixed(2)
                : "N/A"}
            </p>{" "}
            {/* Use DTO field */}
          </div>
        </div>
        <div className="seat-map-container">
          <AirplaneLayout
            onSelectSeat={handleSeatSelect} // Updated handler
            selectedSeats={selectedSeats} // Pass array
            bookedSeats={flightDetails.bookedSeats}
            aircraftModel={flightDetails.aircraftModel} // Pass model for layout logic
            seatLayoutJson={flightDetails.seatLayoutJson} // Pass layout JSON
          />
        </div>
        <div className="footer">
          {/* Display selected seats */}
          <div className="selected-seats-info mb-4">
            Selected Seats:{" "}
            {selectedSeats.length > 0 ? selectedSeats.join(", ") : "None"}
          </div>
          {/* Updated Button */}
          {selectedSeats.length > 0 ? (
            <button
              onClick={handleProceed}
              disabled={loading}
              className="confirm-button"
            >
              {loading
                ? "Processing..."
                : `Proceed (${selectedSeats.length} Seat${
                    selectedSeats.length > 1 ? "s" : ""
                  })`}
            </button>
          ) : (
            <p className="prompt-text">
              Please select one or more seats from the map above.
            </p>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;
