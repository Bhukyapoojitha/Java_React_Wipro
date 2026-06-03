# Spring Security + JWT Implementation Guide
## BankManagementSystem (com.wipro)
> Spring Boot 3.3.0 | Java 17 | MySQL | Existing Entities: Customer, Account, Branch, Transaction

---

## 📌 What You Will Build

By the end of this guide, your app will have:
- A `User` entity for login (email + password + role)
- A `/auth/register` endpoint to register users
- A `/auth/login` endpoint that returns a **JWT token**
- A **JWT filter** that validates every incoming request
- Role-based access control: `ADMIN` and `USER` roles
- Protected routes (e.g., only ADMIN can delete customers/accounts)
- Swagger UI still accessible without a token

---

## 🗂️ New Files You Need to Create

```
src/main/java/com/wipro/BankManagementSystem/
│
├── security/
│   ├── JwtUtil.java                  ← generates & validates JWT tokens
│   ├── JwtAuthenticationFilter.java  ← intercepts every HTTP request
│   └── SecurityConfig.java           ← Spring Security configuration
│
├── entity/
│   └── User.java                     ← new User entity (login credentials)
│
├── repository/
│   └── UserRepository.java           ← JPA repo for User
│
├── service/
│   └── CustomUserDetailsService.java ← loads user from DB for Spring Security
│
├── controller/
│   └── AuthController.java           ← /auth/register and /auth/login
│
└── dto/
    ├── RegisterRequest.java          ← request body for registration
    ├── LoginRequest.java             ← request body for login
    └── JwtResponse.java              ← response body with token
```

---

## STEP 1 — Add Dependencies in `pom.xml`

Open your `pom.xml` and add these three dependencies inside `<dependencies>`:

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT API -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>

<!-- JWT Implementation -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>

<!-- JWT Jackson Support -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

> ✅ After adding, right-click project → **Maven → Update Project** in STS.

---

## STEP 2 — Add JWT Config in `application.properties`

Open `src/main/resources/application.properties` and add at the bottom:

```properties
# JWT Configuration
jwt.secret=BankVerseSecretKey2024BankVerseSecretKey2024BankVerseSecretKey2024
jwt.expiration=86400000
```

> **jwt.secret** — a long random string used to sign tokens (minimum 32 characters for HS256).  
> **jwt.expiration** — token validity in milliseconds. `86400000` = 24 hours.

---

## STEP 3 — Create the `User` Entity

**Path:** `entity/User.java`

```java
package com.wipro.BankManagementSystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;   // will be stored as BCrypt hash

    @Column(nullable = false)
    private String role;       // "ROLE_ADMIN" or "ROLE_USER"
}
```

> **Why a separate User entity?**  
> Your `Customer` entity is a bank customer (has accounts, branches).  
> A `User` is a system login account. They are different things.  
> An ADMIN user could manage all customers. A USER could be a teller.

---

## STEP 4 — Create `UserRepository`

**Path:** `repository/UserRepository.java`

```java
package com.wipro.BankManagementSystem.repository;

import com.wipro.BankManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```

---

## STEP 5 — Create DTOs (Request/Response Objects)

### 5a. `RegisterRequest.java`
**Path:** `dto/RegisterRequest.java`

```java
package com.wipro.BankManagementSystem.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String role; // "ROLE_ADMIN" or "ROLE_USER"
}
```

### 5b. `LoginRequest.java`
**Path:** `dto/LoginRequest.java`

```java
package com.wipro.BankManagementSystem.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
```

### 5c. `JwtResponse.java`
**Path:** `dto/JwtResponse.java`

```java
package com.wipro.BankManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String email;
    private String role;
}
```

---

## STEP 6 — Create `JwtUtil` (Token Generator & Validator)

**Path:** `security/JwtUtil.java`

```java
package com.wipro.BankManagementSystem.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // Generate a signing key from the secret
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Called after login — creates and returns a JWT token
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)               // store email as subject
                .claim("role", role)             // store role inside token
                .setIssuedAt(new Date())         // token created time
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // expiry
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // sign with secret
                .compact();
    }

    // Extract email (subject) from token
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // Extract role from token
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // Validate token: check email matches AND token is not expired
    public boolean validateToken(String token, String email) {
        return extractEmail(token).equals(email) && !isTokenExpired(token);
    }

    // Parse the token and get all claims (payload data)
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
```

> **What is a JWT Token?**  
> A JWT has 3 parts separated by dots: `header.payload.signature`  
> - Header: algorithm type  
> - Payload: your data (email, role, expiry)  
> - Signature: HMAC hash of header+payload using your secret key  
> Nobody can fake a token without knowing your secret.

---

## STEP 7 — Create `CustomUserDetailsService`

**Path:** `service/CustomUserDetailsService.java`

Spring Security needs a service that loads a user from the database by their username (which in your case is email).

```java
package com.wipro.BankManagementSystem.service;

import com.wipro.BankManagementSystem.entity.User;
import com.wipro.BankManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Load user from DB by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Return Spring Security's UserDetails object
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}
```

> **Why this class?**  
> Spring Security doesn't know about your `User` entity.  
> This class acts as a bridge — it fetches your `User` from DB and converts it  
> into Spring Security's `UserDetails` format.

---

## STEP 8 — Create `JwtAuthenticationFilter`

**Path:** `security/JwtAuthenticationFilter.java`

This filter runs **before every HTTP request**. It reads the JWT token from the `Authorization` header, validates it, and tells Spring Security who is logged in.

```java
package com.wipro.BankManagementSystem.security;

import com.wipro.BankManagementSystem.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Read the Authorization header
        String authHeader = request.getHeader("Authorization");

        String token = null;
        String email = null;

        // 2. Check if header starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // remove "Bearer " prefix
            try {
                email = jwtUtil.extractEmail(token); // get email from token
            } catch (Exception e) {
                // token is malformed or expired — just continue without auth
            }
        }

        // 3. If email found AND no authentication set yet
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // 4. Validate the token
            if (jwtUtil.validateToken(token, userDetails.getUsername())) {

                // 5. Create authentication object and set it in SecurityContext
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                // Now Spring Security knows this request is authenticated
            }
        }

        // 6. Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
```

> **How this works (flow):**  
> Client sends request with header: `Authorization: Bearer eyJhbGci...`  
> → Filter reads the token → validates it → sets user in SecurityContext  
> → Spring Security allows/denies the request based on roles

---

## STEP 9 — Create `SecurityConfig`

**Path:** `security/SecurityConfig.java`

This is the **most important class**. It configures which endpoints are public, which require a token, and which require specific roles.

```java
package com.wipro.BankManagementSystem.security;

import com.wipro.BankManagementSystem.service.CustomUserDetailsService;
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
    private JwtAuthenticationFilter jwtAuthFilter;

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
```

> **Note on roles:**  
> Spring Security's `hasRole("ADMIN")` automatically checks for `ROLE_ADMIN`.  
> So when you save `"ROLE_ADMIN"` in the DB, `hasRole("ADMIN")` matches it.

---

## STEP 10 — Create `AuthController`

**Path:** `controller/AuthController.java`

```java
package com.wipro.BankManagementSystem.controller;

import com.wipro.BankManagementSystem.dto.JwtResponse;
import com.wipro.BankManagementSystem.dto.LoginRequest;
import com.wipro.BankManagementSystem.dto.RegisterRequest;
import com.wipro.BankManagementSystem.entity.User;
import com.wipro.BankManagementSystem.repository.UserRepository;
import com.wipro.BankManagementSystem.security.JwtUtil;
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
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already registered");
        }

        // Create new user and encode password
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // BCrypt hash
        user.setRole(request.getRole()); // e.g., "ROLE_ADMIN" or "ROLE_USER"

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    // ─── LOGIN ──────────────────────────────────────────────────
    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {

        // Authenticate — this calls CustomUserDetailsService internally
        // Throws exception if credentials are wrong
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
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
        return ResponseEntity.ok(new JwtResponse(token, userDetails.getUsername(), role));
    }
}
```

---

## STEP 11 — Update Swagger for JWT Support

To make Swagger UI send JWT tokens in requests, update your main application class or create a Swagger config:

**Path:** `config/SwaggerConfig.java` *(create a new `config` folder)*

```java
package com.wipro.BankManagementSystem.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class SwaggerConfig {
    // This enables the "Authorize" button in Swagger UI
    // where you can paste your JWT token
}
```

Then annotate your controllers to require the token in Swagger docs. Add this annotation at **class level** to each controller (AccountRestController, CustomerRestController, etc.):

```java
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")  // add this line
@RestController
@RequestMapping("/account")
public class AccountRestController { ... }
```

---

## STEP 12 — Final `application.properties`

Your complete `application.properties` should look like this:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/bankdb
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

server.port=8080

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html

# JWT
jwt.secret=BankVerseSecretKey2024BankVerseSecretKey2024BankVerseSecretKey2024
jwt.expiration=86400000
```

---

## STEP 13 — Testing the Flow

### Step 13a — Register a User (Postman or Swagger)

**POST** `http://localhost:8080/auth/register`

```json
{
  "email": "admin@bankverse.com",
  "password": "admin123",
  "role": "ROLE_ADMIN"
}
```

Expected response:
```
User registered successfully
```

---

### Step 13b — Login to Get JWT Token

**POST** `http://localhost:8080/auth/login`

```json
{
  "email": "admin@bankverse.com",
  "password": "admin123"
}
```

Expected response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBiYW5rdmVyc2UuY29tIiwi...",
  "email": "admin@bankverse.com",
  "role": "ROLE_ADMIN"
}
```

---

### Step 13c — Use Token for Protected Endpoints

**GET** `http://localhost:8080/customer`

Add header:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBiYW5rdmVyc2UuY29tIiwi...
```

Without the token → `403 Forbidden`  
With valid token → `200 OK` with list of customers

---

### Step 13d — Testing in Swagger UI

1. Open `http://localhost:8080/swagger-ui.html`
2. Go to `/auth/login` → Execute → Copy the token
3. Click the **Authorize** button (top right of Swagger UI)
4. Enter: `Bearer <paste-your-token-here>`
5. Click Authorize → Now all Swagger requests will include the token

---

## 📁 Final Project Structure

```
src/main/java/com/wipro/BankManagementSystem/
│
├── BankManagementSystemApplication.java
│
├── config/
│   └── SwaggerConfig.java              ← NEW
│
├── security/
│   ├── JwtUtil.java                    ← NEW
│   ├── JwtAuthenticationFilter.java    ← NEW
│   └── SecurityConfig.java             ← NEW
│
├── entity/
│   ├── Account.java                    (existing)
│   ├── Branch.java                     (existing)
│   ├── Customer.java                   (existing)
│   ├── Transaction.java                (existing)
│   ├── TransactionType.java            (existing)
│   └── User.java                       ← NEW
│
├── repository/
│   ├── AccountRepository.java          (existing)
│   ├── BranchRepository.java           (existing)
│   ├── CustomerRepository.java         (existing)
│   ├── TransactionRepository.java      (existing)
│   └── UserRepository.java             ← NEW
│
├── service/
│   ├── IAccountService.java            (existing)
│   ├── AccountServiceImpl.java         (existing)
│   ├── IBranchService.java             (existing)
│   ├── BranchServiceImpl.java          (existing)
│   ├── ICustomerService.java           (existing)
│   ├── CustomerServiceImpl.java        (existing)
│   ├── ITransactionService.java        (existing)
│   ├── TransactionServiceImpl.java     (existing)
│   └── CustomUserDetailsService.java   ← NEW
│
├── controller/
│   ├── AccountRestController.java      (existing, add @SecurityRequirement)
│   ├── BranchRestController.java       (existing, add @SecurityRequirement)
│   ├── CustomerRestController.java     (existing, add @SecurityRequirement)
│   ├── TransactionRestController.java  (existing, add @SecurityRequirement)
│   └── AuthController.java             ← NEW
│
├── dto/
│   ├── AccountDTO.java                 (existing)
│   ├── BranchDTO.java                  (existing)
│   ├── CustomerDTO.java                (existing)
│   ├── TransactionDTO.java             (existing)
│   ├── TransactionRequestDTO.java      (existing)
│   ├── RegisterRequest.java            ← NEW
│   ├── LoginRequest.java               ← NEW
│   └── JwtResponse.java                ← NEW
│
└── exception/
    ├── GlobalExceptionHandler.java     (existing)
    └── ResourceNotFoundException.java  (existing)
```

---

## 🔒 Role-Based Access Summary

| Endpoint | ROLE_USER | ROLE_ADMIN |
|----------|-----------|------------|
| `POST /auth/register` | ✅ Public | ✅ Public |
| `POST /auth/login` | ✅ Public | ✅ Public |
| `GET /customer/**` | ✅ Allowed | ✅ Allowed |
| `POST /customer` | ✅ Allowed | ✅ Allowed |
| `PUT /customer/{id}` | ✅ Allowed | ✅ Allowed |
| `DELETE /customer/{id}` | ❌ Forbidden | ✅ Allowed |
| `GET /account/**` | ✅ Allowed | ✅ Allowed |
| `DELETE /account/{id}` | ❌ Forbidden | ✅ Allowed |
| `GET /branch/**` | ❌ Forbidden | ✅ Allowed |
| `POST /branch` | ❌ Forbidden | ✅ Allowed |
| `GET /transaction/**` | ✅ Allowed | ✅ Allowed |
| `DELETE /transaction/{id}` | ❌ Forbidden | ✅ Allowed |

---

## ⚠️ Common Errors & Fixes

### Error: `403 Forbidden` on all requests after adding Security
**Cause:** SecurityConfig is blocking everything.  
**Fix:** Make sure `/auth/**` is listed under `.permitAll()` in `SecurityConfig`.

---

### Error: `Cannot instantiate AuthenticationManager`
**Cause:** `AuthenticationManager` bean not exposed.  
**Fix:** Make sure you have this in `SecurityConfig`:
```java
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
}
```

---

### Error: `JWT signature does not match`
**Cause:** `jwt.secret` in `application.properties` is too short (less than 32 characters for HS256).  
**Fix:** Use a secret that is at least 32 characters long.

---

### Error: `Full authentication is required` in Swagger
**Cause:** You opened Swagger but didn't authorize with your token.  
**Fix:** Click the **Authorize** button in Swagger, paste `Bearer <your-token>`.

---

### Error: `Circular dependency` on startup
**Cause:** `SecurityConfig` and `CustomUserDetailsService` have circular injection.  
**Fix:** Use `@Lazy` on the `JwtAuthenticationFilter` injection in `SecurityConfig`:
```java
@Autowired
@Lazy
private JwtAuthenticationFilter jwtAuthFilter;
```

---

## ✅ Quick Implementation Checklist

- [ ] Added 4 JWT dependencies in `pom.xml`
- [ ] Added `jwt.secret` and `jwt.expiration` in `application.properties`
- [ ] Created `User.java` entity
- [ ] Created `UserRepository.java`
- [ ] Created `RegisterRequest.java`, `LoginRequest.java`, `JwtResponse.java` DTOs
- [ ] Created `JwtUtil.java` in `security/`
- [ ] Created `CustomUserDetailsService.java` in `service/`
- [ ] Created `JwtAuthenticationFilter.java` in `security/`
- [ ] Created `SecurityConfig.java` in `security/`
- [ ] Created `AuthController.java` in `controller/`
- [ ] Created `SwaggerConfig.java` in `config/`
- [ ] Added `@SecurityRequirement(name = "bearerAuth")` to existing controllers
- [ ] Maven → Update Project in STS
- [ ] Run app → `users` table auto-created in MySQL
- [ ] Tested `/auth/register` → success
- [ ] Tested `/auth/login` → got token
- [ ] Tested `/customer` with token → `200 OK`
- [ ] Tested `/customer` without token → `403 Forbidden`
