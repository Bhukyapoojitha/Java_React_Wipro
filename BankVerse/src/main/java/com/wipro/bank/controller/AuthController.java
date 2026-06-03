package com.wipro.bank.controller;


import com.wipro.bank.entity.User;
import com.wipro.bank.repository.UserRepository;
import com.wipro.bank.util.JwtUtil;
import com.wipro.bank.dto.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Register and Login APIs")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // ─── REGISTER ───────────────────────────────────────────────
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {

        // Check if email already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already registered");
        }

        // Create new user and encode password
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // BCrypt hash
        user.setRole(request.getRole()); // e.g., "ROLE_ADMIN" or "ROLE_USER"

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    // ─── LOGIN ──────────────────────────────────────────────────
    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO request) {

        // Authenticate — this calls CustomUserDetailsService internally
        // Throws exception if credentials are wrong
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Get authenticated user details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Get the role
        String role = userDetails.getAuthorities()
                .iterator().next().getAuthority();

        // Generate JWT token
        String token = jwtUtil.generateToken(userDetails.getUsername(), role);

        // Return token in response
        return ResponseEntity.ok(new JwtResponseDTO(token, userDetails.getUsername(), role));
    }
}