package com.airline.airline_reservation_springboot.config;

import com.airline.airline_reservation_springboot.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.config.Customizer;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthenticationSuccessHandler successHandler; // Inject the new handler

    // Update the constructor
    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder,
            CustomAuthenticationSuccessHandler successHandler) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.successHandler = successHandler; // Set the handler
    }


    // Bean for providing user details from the database
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userService.findByEmail(email)
                .map(user -> org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    // Bean for the authentication provider that uses our user details service and
    // password encoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    // The main security filter chain configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // --- THIS IS THE FIRST CHANGE ---
                .cors(Customizer.withDefaults())
                // --- THIS IS THE SECOND CHANGE ---
                // Disable CSRF protection for API-driven frontends
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        // Allow access to static resources (CSS, JS) for everyone
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        // Allow access to public pages for everyone
                        .requestMatchers("/", "/login", "/register", "/search", "/flight-results",
                                "/seat-selection.html")
                        .permitAll()
                        // Allow access to the API endpoints for authenticated users
                        .requestMatchers("/api/**").authenticated()
                        // Restrict access to admin pages to users with the 'Admin' role
                        .requestMatchers("/admin/**").hasRole("Admin")
                        // Restrict access to staff pages to users with 'Admin' or 'Staff' roles
                        .requestMatchers("/staff/**").hasAnyRole("Admin", "Staff")
                        // Any other request must be authenticated
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        // Use our custom login page
                        .loginPage("/login")
                        // The URL to submit the login form to
                        .loginProcessingUrl("/perform_login")
                        // Redirect to the homepage on successful login
                        .successHandler(successHandler)
                        // Redirect back to the login page on failure
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        // The URL to trigger logout
                        .logoutUrl("/logout")
                        // The URL to redirect to after logout
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll());

        return http.build();
    }
}
