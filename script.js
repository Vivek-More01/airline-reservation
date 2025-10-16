document.addEventListener('DOMContentLoaded', () => {
  // Dynamic navbar behavior
  const header = document.querySelector('.site-header');
  let lastScrollY = window.scrollY;
  let ticking = false;

  function onScroll() {
    const currentY = window.scrollY;
    const isScrollingDown = currentY > lastScrollY;

    if (currentY > 8) {
      header?.classList.add('header-scrolled');
    } else {
      header?.classList.remove('header-scrolled');
    }

    if (currentY > 120 && isScrollingDown) {
      header?.classList.add('header-hidden');
    } else {
      header?.classList.remove('header-hidden');
    }

    lastScrollY = currentY;
    ticking = false;
  }

  window.addEventListener('scroll', () => {
    if (!ticking) {
      window.requestAnimationFrame(onScroll);
      ticking = true;
    }
  }, { passive: true });

  const yearEl = document.getElementById('year');
  if (yearEl) yearEl.textContent = new Date().getFullYear();

  const form = document.getElementById('flightSearchForm');
  const tripTypeInputs = document.querySelectorAll('input[name="tripType"]');
  const returnField = document.getElementById('returnField');
  const returnInput = document.getElementById('return');
  const departInput = document.getElementById('depart');
  const hint = document.getElementById('formHint');

  function setMinDates() {
    const today = new Date();
    const yyyy = today.getFullYear();
    const mm = String(today.getMonth() + 1).padStart(2, '0');
    const dd = String(today.getDate()).padStart(2, '0');
    const min = `${yyyy}-${mm}-${dd}`;
    if (departInput) departInput.min = min;
    if (returnInput) returnInput.min = departInput?.value || min;
  }

  setMinDates();

  tripTypeInputs.forEach(radio => {
    radio.addEventListener('change', () => {
      const isRound = radio.value === 'round';
      returnField.hidden = !isRound;
      if (!isRound && returnInput) returnInput.value = '';
    });
  });

  if (departInput && returnInput) {
    departInput.addEventListener('change', () => {
      returnInput.min = departInput.value || returnInput.min;
      if (returnInput.value && returnInput.value < returnInput.min) {
        returnInput.value = '';
      }
    });
  }

  form?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = new FormData(form);
    const from = (data.get('from') || '').toString().trim();
    const to = (data.get('to') || '').toString().trim();
    const depart = (data.get('depart') || '').toString();
    const tripType = (data.get('tripType') || 'oneway').toString();
    const returnDate = (data.get('return') || '').toString();
    const passengers = data.get('passengers');
    const cabin = data.get('cabin');

    if (!from || !to || !depart) {
      hint.textContent = 'Please fill From, To and Depart date.';
      hint.style.color = '#b91c1c';
      return;
    }
    if (tripType === 'round' && !returnDate) {
      hint.textContent = 'Please select a Return date for round-trip.';
      hint.style.color = '#b91c1c';
      return;
    }

    hint.style.color = '#0f766e';
    hint.textContent = 'Searching…';

    try {
      // Call the backend API
      const response = await fetch(`http://localhost:3000/api/flights/search?origin=${encodeURIComponent(from)}&destination=${encodeURIComponent(to)}&departureDate=${encodeURIComponent(depart)}&passengers=${encodeURIComponent(String(passengers))}`);
      
      if (response.ok) {
        const result = await response.json();
        // Redirect to search results page with results
        const url = `search.html?from=${encodeURIComponent(from)}&to=${encodeURIComponent(to)}&depart=${encodeURIComponent(depart)}&trip=${encodeURIComponent(tripType)}&return=${encodeURIComponent(returnDate)}&pax=${encodeURIComponent(String(passengers))}&cabin=${encodeURIComponent(String(cabin))}`;
        window.location.href = url;
      } else {
        hint.textContent = 'Error searching flights. Please try again.';
        hint.style.color = '#b91c1c';
      }
    } catch (error) {
      console.error('Search error:', error);
      hint.textContent = 'Error connecting to server. Please try again.';
      hint.style.color = '#b91c1c';
    }
  });
});
