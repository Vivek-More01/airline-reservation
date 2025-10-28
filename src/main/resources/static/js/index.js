// src/main/resources/static/js/index.js

document.addEventListener("DOMContentLoaded", () => {
  // --- Smooth Scroll for Navigation Links ---
  const navLinks = document.querySelectorAll('nav a.nav-link[href^="/#"]'); // Select links starting with /#

  navLinks.forEach((link) => {
    link.addEventListener("click", function (e) {
      const href = this.getAttribute("href");
      // Ensure it's an internal link on the current page
      if (href.startsWith("/#")) {
        e.preventDefault(); // Prevent default anchor jump
        const targetId = href.substring(2); // Remove '/#' prefix
        const targetElement = document.getElementById(targetId);

        if (targetElement) {
          // Calculate scroll position (consider fixed header height if applicable)
          const headerOffset = 64; // Approx height of sticky header in pixels (h-16) - adjust if needed
          const elementPosition = targetElement.getBoundingClientRect().top;
          const offsetPosition =
            elementPosition + window.pageYOffset - headerOffset;

          // Use smooth scroll behavior
          window.scrollTo({
            top: offsetPosition,
            behavior: "smooth",
          });
        } else {
          console.warn(`Smooth scroll target not found: #${targetId}`);
          // Fallback: If target not found (maybe link error), just go to homepage
          // window.location.href = '/';
        }
      }
      // If it's a normal link (e.g., /login), let the browser handle it
    });
  });

  // --- Optional: Add other page-specific JS here ---
  // Example: Form validation, dynamic content loading, etc.
  console.log("Index page JavaScript loaded.");
});
