package com.airline.airline_reservation_springboot.config;
   
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * A general application configuration class.
 * This class holds beans that are used across the application, like the
 * PasswordEncoder.
 */
@Configuration
public class AppConfig {

    /**
     * Defines the PasswordEncoder bean that will be used for hashing passwords.
     * 
     * @return An instance of BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
