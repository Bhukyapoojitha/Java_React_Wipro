package com.wipro.bank.config;

import com.wipro.bank.service.CustomUserDetailsService;
import com.wipro.bank.security.filter.JwtAuthFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // hashes passwords with BCrypt
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (not needed for stateless REST APIs)
            .csrf(csrf -> csrf.disable())

            // Set session to STATELESS (JWT handles state, not sessions)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Define authorization rules
            .authorizeHttpRequests(auth -> auth

                // Public endpoints — no token needed
                .requestMatchers("/auth/**").permitAll()

                // Swagger UI — allow without token
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()

                // ADMIN-only: DELETE operations
                .requestMatchers(HttpMethod.DELETE, "/customer/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/account/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/branch/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/transaction/**").hasRole("ADMIN")

                // ADMIN-only: Branch management (full control)
                .requestMatchers("/branch/**").hasRole("ADMIN")

                // Both ADMIN and USER can access customer, account, transaction
                .requestMatchers("/customer/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/account/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/transaction/**").hasAnyRole("ADMIN", "USER")

                // Any other endpoint requires authentication
                .anyRequest().authenticated()
            )

            // Register our JWT filter BEFORE Spring's default auth filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}