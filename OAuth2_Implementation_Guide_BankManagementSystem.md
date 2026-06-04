# OAuth2 Implementation Guide
## BankManagementSystem (com.wipro)
> Spring Boot 3.3.0 | Java 17 | Already has: JWT + Spring Security + MySQL

---

## 📌 What You Will Build

By the end of this guide, your app will support:
- **Login with Google** (OAuth2 Social Login)
- **Login with GitHub** (OAuth2 Social Login)
- On successful OAuth2 login → **JWT token is generated** (same token as before)
- The same JWT token is used for all protected API calls
- Your existing `/auth/register` and `/auth/login` (username/password) still works
- Swagger UI still accessible

---

## 🧠 How OAuth2 Works (Simple Explanation)

```
User clicks "Login with Google"
        ↓
Your app redirects to Google's login page
        ↓
User logs in on Google (you never see the password)
        ↓
Google redirects back to your app with a "code"
        ↓
Your app exchanges that "code" for user info (name, email)
        ↓
Your app creates/finds the User in your DB
        ↓
Your app generates a JWT token and sends it to the client
        ↓
Client uses JWT for all future API calls (same as before)
```

> You are NOT storing Google passwords.  
> Google handles the authentication. You just receive the user's email from Google.

---

## 🗂️ New Files You Need to Create

```
src/main/java/com/wipro/BankManagementSystem/
│
├── security/
│   ├── JwtUtil.java                         (existing)
│   ├── JwtAuthenticationFilter.java         (existing)
│   ├── SecurityConfig.java                  (MODIFY this)
│   └── OAuth2SuccessHandler.java            ← NEW
│
├── service/
│   └── CustomOAuth2UserService.java         ← NEW
│
└── (no new DTOs or entities needed —
    OAuth2 users are saved into your existing User entity)
```

---

## STEP 1 — Add OAuth2 Dependency in `pom.xml`

Open `pom.xml` and add this ONE dependency:

```xml
<!-- Spring Security OAuth2 Client -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

> ✅ After adding → STS → right-click project → **Maven → Update Project**

---

## STEP 2 — Create Google & GitHub OAuth2 Apps

You need to register your app with Google and GitHub to get a **Client ID** and **Client Secret**.

---

### 2a — Google Setup

1. Go to: **https://console.cloud.google.com/**
2. Click **Select a Project** → **New Project** → name it `BankVerse`
3. Click **APIs & Services** → **OAuth consent screen**
   - User Type: **External** → click Create
   - App name: `BankVerse`
   - User support email: your email
   - Developer contact: your email
   - Click **Save and Continue** (skip scopes and test users for now)
4. Click **Credentials** → **Create Credentials** → **OAuth 2.0 Client IDs**
   - Application type: **Web application**
   - Name: `BankVerse Web Client`
   - Authorized redirect URIs → click **Add URI**:
     ```
     http://localhost:8080/login/oauth2/code/google
     ```
   - Click **Create**
5. Copy the **Client ID** and **Client Secret** — you will need them in Step 3

---

### 2b — GitHub Setup

1. Go to: **https://github.com/settings/developers**
2. Click **OAuth Apps** → **New OAuth App**
   - Application name: `BankVerse`
   - Homepage URL: `http://localhost:8080`
   - Authorization callback URL:
     ```
     http://localhost:8080/login/oauth2/code/github
     ```
3. Click **Register application**
4. Click **Generate a new client secret**
5. Copy the **Client ID** and **Client Secret**

---

## STEP 3 — Add OAuth2 Config in `application.properties`

Add these lines at the bottom of your `application.properties`:

```properties
# ─── Google OAuth2 ───────────────────────────────────────────
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=email,profile
spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8080/login/oauth2/code/google

# ─── GitHub OAuth2 ───────────────────────────────────────────
spring.security.oauth2.client.registration.github.client-id=YOUR_GITHUB_CLIENT_ID
spring.security.oauth2.client.registration.github.client-secret=YOUR_GITHUB_CLIENT_SECRET
spring.security.oauth2.client.registration.github.scope=read:user,user:email
spring.security.oauth2.client.registration.github.redirect-uri=http://localhost:8080/login/oauth2/code/github
```

> Replace `YOUR_GOOGLE_CLIENT_ID` etc. with the actual values from Step 2.

---

## STEP 4 — Update `User` Entity (Add OAuth2 Fields)

Your existing `User.java` stores email, password, role.  
For OAuth2 users, there is no password. Add two new fields:

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

    @Column
    private String password;       // null for OAuth2 users — that's OK

    @Column(nullable = false)
    private String role;           // "ROLE_USER" by default for OAuth2 users

    @Column
    private String provider;       // "local", "google", or "github"

    @Column
    private String name;           // display name from Google/GitHub
}
```

> ✅ `spring.jpa.hibernate.ddl-auto=update` will auto-add the new columns to your DB.  
> No manual SQL needed.

---

## STEP 5 — Create `CustomOAuth2UserService`

This service is called **automatically by Spring** after Google/GitHub authenticates the user.  
It receives the user's info (email, name) from Google/GitHub and saves them to your DB.

**Path:** `service/CustomOAuth2UserService.java`

```java
package com.wipro.BankManagementSystem.service;

import com.wipro.BankManagementSystem.entity.User;
import com.wipro.BankManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. Call the default service to get user info from Google/GitHub
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. Find out which provider (google or github)
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // 3. Extract email from provider's response
        String email = extractEmail(oAuth2User, provider);

        // 4. Extract name from provider's response
        String name = extractName(oAuth2User, provider);

        // 5. Check if user already exists in our DB
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isEmpty()) {
            // 6a. New user — save them to DB
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(null);         // no password for OAuth2 users
            newUser.setRole("ROLE_USER");      // default role
            newUser.setProvider(provider);
            newUser.setName(name);
            userRepository.save(newUser);
        } else {
            // 6b. Existing user — update their name/provider if needed
            User user = existingUser.get();
            user.setName(name);
            user.setProvider(provider);
            userRepository.save(user);
        }

        // 7. Return the OAuth2User (Spring Security needs this)
        return oAuth2User;
    }

    // Google gives email under "email" key
    // GitHub gives email under "email" key (if public) or may need extra call
    private String extractEmail(OAuth2User oAuth2User, String provider) {
        if ("google".equals(provider)) {
            return oAuth2User.getAttribute("email");
        } else if ("github".equals(provider)) {
            // GitHub email may be null if user set it private
            String email = oAuth2User.getAttribute("email");
            if (email == null) {
                // Fallback: use GitHub username as email placeholder
                String login = oAuth2User.getAttribute("login");
                return login + "@github.com";
            }
            return email;
        }
        return oAuth2User.getAttribute("email");
    }

    private String extractName(OAuth2User oAuth2User, String provider) {
        if ("google".equals(provider)) {
            return oAuth2User.getAttribute("name");
        } else if ("github".equals(provider)) {
            String name = oAuth2User.getAttribute("name");
            if (name == null) {
                return oAuth2User.getAttribute("login");
            }
            return name;
        }
        return oAuth2User.getAttribute("name");
    }
}
```

---

## STEP 6 — Create `OAuth2SuccessHandler`

This is called **after successful OAuth2 login**.  
It generates a JWT token and **redirects the user** with the token in the URL.

**Path:** `security/OAuth2SuccessHandler.java`

```java
package com.wipro.BankManagementSystem.security;

import com.wipro.BankManagementSystem.entity.User;
import com.wipro.BankManagementSystem.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // 1. Get the authenticated OAuth2 user
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 2. Get email from OAuth2 user attributes
        String email = oAuth2User.getAttribute("email");

        // Handle GitHub users who may have null email
        if (email == null) {
            String login = oAuth2User.getAttribute("login");
            email = login + "@github.com";
        }

        // 3. Load user from DB to get their role
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found after OAuth2 login"));

        // 4. Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        // 5. Redirect to a frontend URL with token as query param
        // For now we redirect to Swagger with token info
        // In production, redirect to your React/Angular frontend
        String redirectUrl = "/oauth2/success?token=" + token
                + "&email=" + user.getEmail()
                + "&role=" + user.getRole();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
```

---

## STEP 7 — Create `OAuth2Controller` (Token Display Endpoint)

This endpoint receives the redirect after OAuth2 login and shows the JWT token.  
In production, your frontend (React/Angular) would handle this redirect.

**Path:** `controller/OAuth2Controller.java`

```java
package com.wipro.BankManagementSystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    // This endpoint is called after successful OAuth2 login
    // It receives the JWT token as a query param and returns it as JSON
    @GetMapping("/success")
    public ResponseEntity<Map<String, String>> oauthSuccess(
            @RequestParam String token,
            @RequestParam String email,
            @RequestParam String role) {

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("email", email);
        response.put("role", role);
        response.put("message", "OAuth2 login successful! Use this token for API calls.");

        return ResponseEntity.ok(response);
    }
}
```

---

## STEP 8 — Update `SecurityConfig.java`

This is the most important change. Add OAuth2 login configuration to your existing `SecurityConfig`.

**Path:** `security/SecurityConfig.java`

```java
package com.wipro.BankManagementSystem.security;

import com.wipro.BankManagementSystem.service.CustomOAuth2UserService;
import com.wipro.BankManagementSystem.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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
    @Lazy
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;   // ← NEW

    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;         // ← NEW

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
            .csrf(csrf -> csrf.disable())

            // NOTE: OAuth2 login NEEDS sessions temporarily during the redirect flow
            // So we use STATELESS only for JWT routes, not the OAuth2 redirect
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

            .authorizeHttpRequests(auth -> auth

                // ✅ Public — no token needed
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/oauth2/**").permitAll()

                // ✅ Swagger
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()

                // ✅ OAuth2 login redirect URLs — must be public
                .requestMatchers("/login/oauth2/**", "/oauth2/authorization/**").permitAll()

                // 🔒 ADMIN only — DELETE operations
                .requestMatchers(HttpMethod.DELETE, "/customer/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/account/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/branch/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/transaction/**").hasRole("ADMIN")

                // 🔒 ADMIN only — Branch full control
                .requestMatchers("/branch/**").hasRole("ADMIN")

                // 🔒 Both roles
                .requestMatchers("/customer/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/account/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/transaction/**").hasAnyRole("ADMIN", "USER")

                .anyRequest().authenticated()
            )

            // ─── OAuth2 Login Configuration ──────────────────────────
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo ->
                    userInfo.userService(customOAuth2UserService)  // our custom service
                )
                .successHandler(oAuth2SuccessHandler)              // our success handler
            )

            // ─── JWT Filter ──────────────────────────────────────────
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

---

## STEP 9 — Full `application.properties`

Your complete file should look like this:

```properties
# ─── Database ────────────────────────────────────────────────
spring.datasource.url=jdbc:mysql://localhost:3306/bankdb
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

server.port=8080

# ─── Swagger ─────────────────────────────────────────────────
springdoc.swagger-ui.path=/swagger-ui.html

# ─── JWT ─────────────────────────────────────────────────────
jwt.secret=BankVerseSecretKey2024BankVerseSecretKey2024BankVerseSecretKey2024
jwt.expiration=86400000

# ─── Google OAuth2 ───────────────────────────────────────────
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=email,profile
spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8080/login/oauth2/code/google

# ─── GitHub OAuth2 ───────────────────────────────────────────
spring.security.oauth2.client.registration.github.client-id=YOUR_GITHUB_CLIENT_ID
spring.security.oauth2.client.registration.github.client-secret=YOUR_GITHUB_CLIENT_SECRET
spring.security.oauth2.client.registration.github.scope=read:user,user:email
spring.security.oauth2.client.registration.github.redirect-uri=http://localhost:8080/login/oauth2/code/github
```

---

## STEP 10 — Testing the OAuth2 Flow

### Method 1 — Browser Test (Easiest)

1. Start your Spring Boot app
2. Open browser → go to:
   ```
   http://localhost:8080/oauth2/authorization/google
   ```
3. Google login page opens → log in with your Google account
4. Google redirects back → your `OAuth2SuccessHandler` runs
5. Browser lands on:
   ```
   http://localhost:8080/oauth2/success?token=eyJhb...&email=you@gmail.com&role=ROLE_USER
   ```
6. You see JSON with your JWT token:
   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiJ9...",
     "email": "you@gmail.com",
     "role": "ROLE_USER",
     "message": "OAuth2 login successful! Use this token for API calls."
   }
   ```
7. Copy the token → use it in Swagger or Postman for API calls

For GitHub:
```
http://localhost:8080/oauth2/authorization/github
```

---

### Method 2 — Postman Test

After getting the token from browser step above:

**GET** `http://localhost:8080/customer`

Header:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

Expected: `200 OK` with list of customers ✅

---

### Method 3 — Swagger UI Test

1. Open: `http://localhost:8080/swagger-ui/index.html`
2. First do the browser OAuth2 flow (Step above) to get token
3. Click **Authorize 🔒** button in Swagger
4. Enter: `Bearer <your-oauth2-jwt-token>`
5. Now all locked APIs work ✅

---

## 📊 Both Login Methods — Side by Side

| Feature | JWT Login (`/auth/login`) | OAuth2 Login (Google/GitHub) |
|---------|--------------------------|------------------------------|
| User types password? | ✅ Yes | ❌ No |
| Password stored in DB? | ✅ Yes (BCrypt) | ❌ No (null) |
| Who verifies password? | Your app | Google / GitHub |
| Token returned? | ✅ JWT | ✅ JWT (same format) |
| Token used for APIs? | ✅ Yes | ✅ Yes (same way) |
| Role assigned? | From DB | `ROLE_USER` by default |
| Works in Postman? | ✅ Yes | ✅ Yes (after browser login) |

---

## 📁 Final Project Structure (after both JWT + OAuth2)

```
src/main/java/com/wipro/BankManagementSystem/
│
├── config/
│   └── SwaggerConfig.java                   (existing)
│
├── security/
│   ├── JwtUtil.java                         (existing)
│   ├── JwtAuthenticationFilter.java         (existing)
│   ├── SecurityConfig.java                  (MODIFIED — added OAuth2)
│   └── OAuth2SuccessHandler.java            ← NEW
│
├── entity/
│   ├── Account.java                         (existing)
│   ├── Branch.java                          (existing)
│   ├── Customer.java                        (existing)
│   ├── Transaction.java                     (existing)
│   ├── TransactionType.java                 (existing)
│   └── User.java                            (MODIFIED — added provider, name)
│
├── repository/
│   ├── AccountRepository.java               (existing)
│   ├── BranchRepository.java                (existing)
│   ├── CustomerRepository.java              (existing)
│   ├── TransactionRepository.java           (existing)
│   └── UserRepository.java                  (existing)
│
├── service/
│   ├── IAccountService.java                 (existing)
│   ├── AccountServiceImpl.java              (existing)
│   ├── IBranchService.java                  (existing)
│   ├── BranchServiceImpl.java               (existing)
│   ├── ICustomerService.java                (existing)
│   ├── CustomerServiceImpl.java             (existing)
│   ├── ITransactionService.java             (existing)
│   ├── TransactionServiceImpl.java          (existing)
│   ├── CustomUserDetailsService.java        (existing)
│   └── CustomOAuth2UserService.java         ← NEW
│
├── controller/
│   ├── AccountRestController.java           (existing)
│   ├── BranchRestController.java            (existing)
│   ├── CustomerRestController.java          (existing)
│   ├── TransactionRestController.java       (existing)
│   ├── AuthController.java                  (existing)
│   └── OAuth2Controller.java               ← NEW
│
└── dto/
    ├── AccountDTO.java                      (existing)
    ├── BranchDTO.java                       (existing)
    ├── CustomerDTO.java                     (existing)
    ├── TransactionDTO.java                  (existing)
    ├── TransactionRequestDTO.java           (existing)
    ├── RegisterRequest.java                 (existing)
    ├── LoginRequest.java                    (existing)
    └── JwtResponse.java                     (existing)
```

---

## ⚠️ Common Errors & Fixes

### Error: `redirect_uri_mismatch` from Google
**Cause:** The redirect URI in Google Console doesn't match exactly.  
**Fix:** Make sure Google Console has EXACTLY:
```
http://localhost:8080/login/oauth2/code/google
```
No trailing slash. No https. Copy-paste it.

---

### Error: `401 Unauthorized` after OAuth2 login
**Cause:** OAuth2 success endpoint `/oauth2/success` is not in `.permitAll()`.  
**Fix:** Add `/oauth2/**` to `permitAll()` in `SecurityConfig`.

---

### Error: App starts but OAuth2 URLs give 404
**Cause:** `spring-boot-starter-oauth2-client` dependency not added.  
**Fix:** Add the dependency and do Maven Update Project.

---

### Error: GitHub email is `null`
**Cause:** GitHub user has set their email as private.  
**Fix:** Already handled in `CustomOAuth2UserService` — falls back to `login@github.com`.

---

### Error: `SessionCreationPolicy.STATELESS` breaks OAuth2 redirect
**Cause:** OAuth2 login flow needs a temporary session during the redirect.  
**Fix:** Already handled — we changed to `IF_REQUIRED` in `SecurityConfig`.  
JWT validation still works perfectly with this setting.

---

### Error: `NullPointerException` in `OAuth2SuccessHandler`
**Cause:** Email attribute is null (GitHub private email).  
**Fix:** Already handled with the null check in `OAuth2SuccessHandler`.

---

## ✅ Quick Implementation Checklist

- [ ] Added `spring-boot-starter-oauth2-client` in `pom.xml`
- [ ] Maven → Update Project in STS
- [ ] Created Google OAuth2 App → copied Client ID & Secret
- [ ] Created GitHub OAuth2 App → copied Client ID & Secret
- [ ] Updated `application.properties` with Google & GitHub credentials
- [ ] Modified `User.java` — added `provider` and `name` fields
- [ ] Created `CustomOAuth2UserService.java`
- [ ] Created `OAuth2SuccessHandler.java`
- [ ] Created `OAuth2Controller.java`
- [ ] Modified `SecurityConfig.java` — added `.oauth2Login(...)` block
- [ ] Run app → `provider` and `name` columns auto-added to `users` table in MySQL
- [ ] Tested Google: `http://localhost:8080/oauth2/authorization/google`
- [ ] Got JWT token from redirect → tested API call with it
- [ ] Tested GitHub: `http://localhost:8080/oauth2/authorization/github`
- [ ] Verified existing `/auth/login` still works as before
