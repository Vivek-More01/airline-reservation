import React, { useState, useEffect } from "react";
import AirplaneLayout from "./AirplaneLayout";
import "./App.css";

function App() {
  // Add state to hold the current user
  const [currentUser, setCurrentUser] = useState(null);
  const [flightDetails, setFlightDetails] = useState(null);
  const [selectedSeat, setSelectedSeat] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // This useEffect runs once when the component loads
  useEffect(() => {
    // --- NEW: Check who is logged in ---
    fetch("http://localhost:8080/api/user/me", {
      credentials: "include", // Crucial: tells the browser to send cookies
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }
        console.log("User session:");
        return null; // Not logged in
      })
      .then((user) => {
        console.log("User session:", user);
        setCurrentUser(user); // Store the user, or null if not logged in
      })
      .catch((err) => console.error("Error fetching user session:", err));

    // --- Existing code to fetch flight details ---
    const params = new URLSearchParams(window.location.search);
    const flightId = params.get("flightId");
    if (!flightId) {
      setError("No flight ID provided in the URL.");
      setLoading(false);
      return;
    }

    fetch(`http://localhost:8080/api/flights/${flightId}`, {
      credentials: "include", // Crucial: send cookies with this request too
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
        console.error("Error fetching flight details:", err);
        setLoading(false);
      });
  }, []);

  const handleBooking = () => {
    if (!currentUser) {
      alert("Session expired. Please log in again to book a flight.");
      // Redirect to the login page, passing a 'return' URL
      window.location.href = `http://localhost:8080/login?returnUrl=${window.location.href}`;
      return;
    }
    if (!selectedSeat) {
      alert("Please select a seat first.");
      return;
    }
    setLoading(true);

    fetch("http://localhost:8080/api/bookings", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include", // Crucial: send cookies with the booking request
      body: JSON.stringify({
        flightId: flightDetails.flightId,
        seatNumber: selectedSeat,
      }),
    })
      .then((response) => {
        if (!response.ok) {
          return response.text().then((text) => {
            throw new Error(text || "Booking failed");
          });
        }
        return response.json();
      })
      .then((newBooking) => {
        window.location.href = `http://localhost:8080/booking-confirmation/${newBooking.bookingId}`;
      })
      .catch((error) => {
        alert(`Booking failed: ${error.message}`);
        setLoading(false);
      });
  };

  if (loading) return <div className="status-message">Loading...</div>;
  if (error) return <div className="status-message error">{error}</div>;

  return (
    <div className="container">
      {/* Show a welcome message if the user is logged in */}
      {currentUser && (
        <div className="welcome-banner">Welcome, {currentUser.name}!</div>
      )}

      <div className="card">
        {/* ... rest of your JSX remains the same ... */}
        <div className="header">
          <h1>Select Your Seat</h1>
          <div className="flight-info">
            <p>
              {flightDetails.airline}: {flightDetails.source} to{" "}
              {flightDetails.destination}
            </p>
            <p className="price">Price: ${flightDetails.price.toFixed(2)}</p>
          </div>
        </div>
        <div className="seat-map-container">
          <AirplaneLayout
            onSelectSeat={setSelectedSeat}
            selectedSeat={selectedSeat}
            bookedSeats={flightDetails.bookedSeats}
          />
        </div>
        <div className="footer">
          {selectedSeat ? (
            <button
              onClick={handleBooking}
              disabled={loading}
              className="confirm-button"
            >
              {loading
                ? "Processing..."
                : `Confirm Booking for Seat ${selectedSeat}`}
            </button>
          ) : (
            <p className="prompt-text">
              Please select a seat from the map above.
            </p>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;
