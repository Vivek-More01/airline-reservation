document.addEventListener('DOMContentLoaded', async () => {
  const yearEl = document.getElementById('year');
  if (yearEl) yearEl.textContent = new Date().getFullYear();

  // Get search parameters from URL
  const urlParams = new URLSearchParams(window.location.search);
  const searchParams = {
    from: urlParams.get('from') || '',
    to: urlParams.get('to') || '',
    depart: urlParams.get('depart') || '',
    trip: urlParams.get('trip') || 'oneway',
    return: urlParams.get('return') || '',
    passengers: parseInt(urlParams.get('pax')) || 1,
    cabin: urlParams.get('cabin') || 'economy'
  };

  // Update page title and subtitle
  updateSearchSummary(searchParams);

  // Search for flights
  await searchFlights(searchParams);

  // Set up event listeners
  setupEventListeners();

  function updateSearchSummary(params) {
    const titleEl = document.getElementById('searchTitle');
    const subtitleEl = document.getElementById('searchSubtitle');
    
    if (titleEl) {
      titleEl.textContent = `${params.from} → ${params.to}`;
    }
    
    if (subtitleEl) {
      const departDate = new Date(params.depart).toLocaleDateString();
      const passengerText = params.passengers === 1 ? '1 passenger' : `${params.passengers} passengers`;
      subtitleEl.textContent = `${departDate} • ${passengerText} • ${params.cabin}`;
    }
  }

  async function searchFlights(params) {
    const resultsContainer = document.getElementById('flightResults');
    const resultsCount = document.getElementById('resultsCount');

    try {
      // Show loading state
      resultsContainer.innerHTML = `
        <div class="loading">
          <div class="spinner"></div>
          <p>Searching for flights...</p>
        </div>
      `;

      // Call the backend API
      const response = await fetch(
        `http://localhost:3000/api/flights/search?origin=${encodeURIComponent(params.from)}&destination=${encodeURIComponent(params.to)}&departureDate=${encodeURIComponent(params.depart)}&passengers=${params.passengers}`
      );

      if (!response.ok) {
        throw new Error('Failed to fetch flights');
      }

      const data = await response.json();
      const flights = data.success ? data.data : [];

      // Update results count
      if (resultsCount) {
        resultsCount.textContent = `${flights.length} flights found`;
      }

      // Display results
      if (flights.length === 0) {
        displayNoResults(resultsContainer);
      } else {
        displayFlights(flights, resultsContainer);
        populateAirlineFilters(flights);
      }

    } catch (error) {
      console.error('Search error:', error);
      displayError(resultsContainer);
    }
  }

  function displayFlights(flights, container) {
    const flightCards = flights.map(flight => createFlightCard(flight)).join('');
    container.innerHTML = flightCards;
  }

  function createFlightCard(flight) {
    const departureTime = new Date(flight.departureTime);
    const arrivalTime = new Date(flight.arrivalTime);
    const airlineInitials = flight.airline.split(' ').map(word => word[0]).join('').toUpperCase();

    return `
      <div class="flight-card" data-flight-id="${flight._id}">
        <div class="flight-header">
          <div class="airline-info">
            <div class="airline-logo">${airlineInitials}</div>
            <div>
              <div class="airline-name">${flight.airline}</div>
              <div class="flight-number">${flight.flightNumber}</div>
            </div>
          </div>
          <div class="price-info">
            <div class="price">$${flight.price}</div>
            <div class="price-per-person">per person</div>
          </div>
        </div>
        
        <div class="flight-details">
          <div class="departure">
            <div class="time">${formatTime(departureTime)}</div>
            <div class="airport">${flight.origin}</div>
          </div>
          
          <div class="flight-path">
            <div class="duration">${flight.duration}</div>
            <div class="path-line"></div>
          </div>
          
          <div class="arrival">
            <div class="time">${formatTime(arrivalTime)}</div>
            <div class="airport">${flight.destination}</div>
          </div>
        </div>
        
        <div class="flight-footer">
          <div class="flight-tags">
            <span class="tag">${flight.aircraft}</span>
            <span class="tag">${flight.availableSeats} seats left</span>
          </div>
          <button class="select-flight" onclick="selectFlight('${flight._id}')">
            Select Flight
          </button>
        </div>
      </div>
    `;
  }

  function displayNoResults(container) {
    container.innerHTML = `
      <div class="no-results">
        <h3>No flights found</h3>
        <p>Try adjusting your search criteria or check different dates.</p>
        <a href="index.html" class="btn btn-primary">Search Again</a>
      </div>
    `;
  }

  function displayError(container) {
    container.innerHTML = `
      <div class="no-results">
        <h3>Search Error</h3>
        <p>We're having trouble finding flights right now. Please try again later.</p>
        <a href="index.html" class="btn btn-primary">Try Again</a>
      </div>
    `;
  }

  function populateAirlineFilters(flights) {
    const airlines = [...new Set(flights.map(flight => flight.airline))];
    const container = document.getElementById('airlineFilters');
    
    if (container) {
      container.innerHTML = airlines.map(airline => `
        <label class="checkbox">
          <input type="checkbox" value="${airline}" checked />
          <span>${airline}</span>
        </label>
      `).join('');
    }
  }

  function setupEventListeners() {
    // Sort functionality
    const sortSelect = document.getElementById('sortBy');
    if (sortSelect) {
      sortSelect.addEventListener('change', (e) => {
        sortFlights(e.target.value);
      });
    }

    // Price range filter
    const priceRange = document.getElementById('priceRange');
    const maxPriceLabel = document.getElementById('maxPrice');
    if (priceRange && maxPriceLabel) {
      priceRange.addEventListener('input', (e) => {
        maxPriceLabel.textContent = `$${e.target.value}`;
        filterFlights();
      });
    }

    // Airline filters
    document.addEventListener('change', (e) => {
      if (e.target.type === 'checkbox' && e.target.closest('#airlineFilters')) {
        filterFlights();
      }
    });

    // Time filters
    document.addEventListener('change', (e) => {
      if (e.target.type === 'checkbox' && e.target.value.match(/morning|afternoon|evening/)) {
        filterFlights();
      }
    });
  }

  function sortFlights(sortBy) {
    const flightCards = Array.from(document.querySelectorAll('.flight-card'));
    const container = document.getElementById('flightResults');
    
    flightCards.sort((a, b) => {
      switch (sortBy) {
        case 'price':
          const priceA = parseFloat(a.querySelector('.price').textContent.replace('$', ''));
          const priceB = parseFloat(b.querySelector('.price').textContent.replace('$', ''));
          return priceA - priceB;
        case 'departure':
          const timeA = a.querySelector('.departure .time').textContent;
          const timeB = b.querySelector('.departure .time').textContent;
          return timeA.localeCompare(timeB);
        case 'duration':
          const durationA = a.querySelector('.duration').textContent;
          const durationB = b.querySelector('.duration').textContent;
          return durationA.localeCompare(durationB);
        default:
          return 0;
      }
    });

    // Re-append sorted cards
    flightCards.forEach(card => container.appendChild(card));
  }

  function filterFlights() {
    const maxPrice = parseInt(document.getElementById('priceRange').value);
    const selectedAirlines = Array.from(document.querySelectorAll('#airlineFilters input:checked'))
      .map(input => input.value);
    const selectedTimes = Array.from(document.querySelectorAll('input[type="checkbox"][value^="morning"], input[type="checkbox"][value^="afternoon"], input[type="checkbox"][value^="evening"]:checked'))
      .map(input => input.value);

    const flightCards = document.querySelectorAll('.flight-card');
    let visibleCount = 0;

    flightCards.forEach(card => {
      const price = parseFloat(card.querySelector('.price').textContent.replace('$', ''));
      const airline = card.querySelector('.airline-name').textContent;
      const departureTime = card.querySelector('.departure .time').textContent;
      
      let shouldShow = true;

      // Price filter
      if (price > maxPrice) {
        shouldShow = false;
      }

      // Airline filter
      if (selectedAirlines.length > 0 && !selectedAirlines.includes(airline)) {
        shouldShow = false;
      }

      // Time filter
      if (selectedTimes.length > 0) {
        const hour = parseInt(departureTime.split(':')[0]);
        const period = departureTime.includes('PM') ? 'PM' : 'AM';
        const hour24 = period === 'PM' && hour !== 12 ? hour + 12 : (period === 'AM' && hour === 12 ? 0 : hour);
        
        const timeCategory = hour24 < 12 ? 'morning' : hour24 < 18 ? 'afternoon' : 'evening';
        if (!selectedTimes.includes(timeCategory)) {
          shouldShow = false;
        }
      }

      card.style.display = shouldShow ? 'block' : 'none';
      if (shouldShow) visibleCount++;
    });

    // Update results count
    const resultsCount = document.getElementById('resultsCount');
    if (resultsCount) {
      resultsCount.textContent = `${visibleCount} flights found`;
    }
  }

  function formatTime(date) {
    return date.toLocaleTimeString('en-US', {
      hour: 'numeric',
      minute: '2-digit',
      hour12: true
    });
  }

  // Global function for flight selection
  window.selectFlight = function(flightId) {
    // In a real app, this would handle booking flow
    alert(`Flight ${flightId} selected! This would proceed to booking.`);
  };
});
