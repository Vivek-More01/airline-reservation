// API Base URL
const API_BASE_URL = 'http://localhost:3000/api';

document.addEventListener('DOMContentLoaded', () => {
  const yearEl = document.getElementById('year');
  if (yearEl) yearEl.textContent = new Date().getFullYear();

  // Check for OAuth error in URL
  const urlParams = new URLSearchParams(window.location.search);
  const oauthError = urlParams.get('error');
  if (oauthError === 'oauth_failed') {
    showGlobalHint('Social login failed. Please try again.', 'error');
  }

  const loginForm = document.getElementById('loginForm');
  const loginHint = document.getElementById('loginHint');
  const loginBtn = document.getElementById('loginBtn');

  if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      
      const formData = new FormData(loginForm);
      const email = formData.get('email')?.toString().trim();
      const password = formData.get('password')?.toString();

      // Basic validation
      if (!email || !password) {
        showHint('Please fill in all fields.', 'error');
        return;
      }

      if (!isValidEmail(email)) {
        showHint('Please enter a valid email address.', 'error');
        return;
      }

      // Disable button and show loading
      loginBtn.disabled = true;
      loginBtn.textContent = 'Signing in...';
      showHint('Signing you in...', 'info');

      try {
        // Call actual API
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ email, password })
        });

        const data = await response.json();

        if (data.success) {
          // Store token
          localStorage.setItem('skyway_token', data.data.token);
          
          showHint('Sign in successful! Redirecting...', 'success');
          
          // Redirect after successful login
          setTimeout(() => {
            window.location.href = 'index.html';
          }, 1500);
        } else {
          showHint(data.message || 'Sign in failed. Please try again.', 'error');
        }
        
      } catch (error) {
        console.error('Login error:', error);
        showHint('Network error. Please try again.', 'error');
      } finally {
        loginBtn.disabled = false;
        loginBtn.textContent = 'Sign in';
      }
    });
  }

  const signupForm = document.getElementById('signupForm');
  const signupHint = document.getElementById('signupHint');
  const signupBtn = document.getElementById('signupBtn');

  if (signupForm) {
    signupForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      
      const formData = new FormData(signupForm);
      const firstName = formData.get('firstName')?.toString().trim();
      const lastName = formData.get('lastName')?.toString().trim();
      const email = formData.get('email')?.toString().trim();
      const password = formData.get('password')?.toString();
      const confirmPassword = formData.get('confirmPassword')?.toString();

      // Validation
      if (!firstName || !lastName || !email || !password || !confirmPassword) {
        showSignupHint('Please fill in all fields.', 'error');
        return;
      }

      if (!isValidEmail(email)) {
        showSignupHint('Please enter a valid email address.', 'error');
        return;
      }

      if (password.length < 8) {
        showSignupHint('Password must be at least 8 characters long.', 'error');
        return;
      }

      if (password !== confirmPassword) {
        showSignupHint('Passwords do not match.', 'error');
        return;
      }

      // Disable button and show loading
      signupBtn.disabled = true;
      signupBtn.textContent = 'Creating account...';
      showSignupHint('Creating your account...', 'info');

      try {
        // Call actual API
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ firstName, lastName, email, password })
        });

        const data = await response.json();

        if (data.success) {
          showSignupHint('Account created successfully! Redirecting to sign in...', 'success');
          
          // Redirect after successful signup
          setTimeout(() => {
            window.location.href = '../login.html';
          }, 1500);
        } else {
          showSignupHint(data.message || 'Account creation failed. Please try again.', 'error');
        }
        
      } catch (error) {
        console.error('Signup error:', error);
        showSignupHint('Network error. Please try again.', 'error');
      } finally {
        signupBtn.disabled = false;
        signupBtn.textContent = 'Create account';
      }
    });
  }

  function showHint(message, type) {
    if (!loginHint) return;
    
    loginHint.textContent = message;
    loginHint.style.color = getHintColor(type);
  }

  function showSignupHint(message, type) {
    if (!signupHint) return;
    
    signupHint.textContent = message;
    signupHint.style.color = getHintColor(type);
  }

  function getHintColor(type) {
    switch (type) {
      case 'error': return '#b91c1c';
      case 'success': return '#0f766e';
      case 'info': return '#0369a1';
      default: return '#64748b';
    }
  }

  function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  // Global hint function for general messages
  function showGlobalHint(message, type) {
    // Create a temporary hint element if none exists
    let hintEl = document.getElementById('globalHint');
    if (!hintEl) {
      hintEl = document.createElement('div');
      hintEl.id = 'globalHint';
      hintEl.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 12px 20px;
        border-radius: 8px;
        font-size: 14px;
        z-index: 9999;
        max-width: 300px;
      `;
      document.body.appendChild(hintEl);
    }
    
    hintEl.textContent = message;
    hintEl.style.backgroundColor = getHintColor(type) === '#b91c1c' ? '#fee2e2' : 
                                   getHintColor(type) === '#0f766e' ? '#d1fae5' : '#dbeafe';
    hintEl.style.color = getHintColor(type);
    hintEl.style.border = `1px solid ${getHintColor(type)}`;
    
    // Auto-hide after 5 seconds
    setTimeout(() => {
      if (hintEl.parentNode) {
        hintEl.parentNode.removeChild(hintEl);
      }
    }, 5000);
  }
});

// Global OAuth functions (outside of DOMContentLoaded)
function signInWithGoogle() {
  window.location.href = `${API_BASE_URL}/auth/google`;
}

function signInWithFacebook() {
  window.location.href = `${API_BASE_URL}/auth/facebook`;
}
