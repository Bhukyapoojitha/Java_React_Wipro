# FinGenie AI – Complete Spring Boot Backend Implementation Guide
### Spring Tool Suite (STS) | Step-by-Step from Scratch

---

## TABLE OF CONTENTS

1. [Project Setup in STS](#1-project-setup-in-sts)
2. [Project Structure Overview](#2-project-structure-overview)
3. [pom.xml Dependencies](#3-pomxml-dependencies)
4. [application.properties Configuration](#4-applicationproperties-configuration)
5. [Entity Layer](#5-entity-layer)
6. [Repository Layer](#6-repository-layer)
7. [DTO Layer](#7-dto-layer)
8. [Security – JWT Implementation](#8-security--jwt-implementation)
9. [Config Classes](#9-config-classes)
10. [Service Layer](#10-service-layer)
11. [Controller Layer](#11-controller-layer)
12. [AI Integration](#12-ai-integration)
13. [Exception Handling](#13-exception-handling)
14. [Swagger Configuration](#14-swagger-configuration)
15. [Database Schema (MySQL)](#15-database-schema-mysql)
16. [Run & Test](#16-run--test)

---

## 1. Project Setup in STS

### Step 1.1 – Create Spring Boot Project

1. Open **Spring Tool Suite (STS)**
2. Go to `File → New → Spring Starter Project`
3. Fill in the details:

```
Name          : fingenie-backend
Group         : com.fingenie
Artifact      : fingenie-backend
Packaging     : Jar
Java Version  : 17
Spring Boot   : 3.2.x
```

4. Click **Next** and add the following dependencies:
   - Spring Web
   - Spring Data JPA
   - Spring Security
   - MySQL Driver
   - Lombok
   - Validation
   - Spring Boot DevTools

5. Click **Finish**

---

## 2. Project Structure Overview

Create the following package structure under `src/main/java/com/fingenie/`:

```
com.fingenie
├── FingenieBackendApplication.java
├── config/
│   ├── SecurityConfig.java
│   ├── JwtConfig.java
│   ├── SwaggerConfig.java
│   └── CorsConfig.java
├── security/
│   ├── JwtAuthFilter.java
│   ├── JwtTokenProvider.java
│   └── UserDetailsServiceImpl.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── InsufficientFundsException.java
│   └── FraudDetectedException.java
├── enums/
│   ├── Role.java
│   ├── TransactionType.java
│   ├── TransactionStatus.java
│   ├── LoanStatus.java
│   └── RiskLevel.java
├── entity/
│   ├── User.java
│   ├── Account.java
│   ├── Transaction.java
│   ├── FraudAlert.java
│   ├── LoanApplication.java
│   ├── Investment.java
│   └── ChatMessage.java
├── repository/
│   ├── UserRepository.java
│   ├── AccountRepository.java
│   ├── TransactionRepository.java
│   ├── FraudAlertRepository.java
│   ├── LoanApplicationRepository.java
│   ├── InvestmentRepository.java
│   └── ChatMessageRepository.java
├── dto/
│   ├── request/
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   ├── TransactionRequest.java
│   │   ├── LoanRequest.java
│   │   └── ChatRequest.java
│   └── response/
│       ├── AuthResponse.java
│       ├── AccountResponse.java
│       ├── TransactionResponse.java
│       ├── FraudAlertResponse.java
│       ├── LoanResponse.java
│       └── ApiResponse.java
├── service/
│   ├── AuthService.java
│   ├── AccountService.java
│   ├── TransactionService.java
│   ├── FraudDetectionService.java
│   ├── LoanService.java
│   ├── InvestmentService.java
│   └── ChatbotService.java
├── serviceImpl/
│   ├── AuthServiceImpl.java
│   ├── AccountServiceImpl.java
│   ├── TransactionServiceImpl.java
│   ├── FraudDetectionServiceImpl.java
│   ├── LoanServiceImpl.java
│   ├── InvestmentServiceImpl.java
│   └── ChatbotServiceImpl.java
├── controller/
│   ├── AuthController.java
│   ├── AccountController.java
│   ├── TransactionController.java
│   ├── FraudController.java
│   ├── LoanController.java
│   ├── InvestmentController.java
│   ├── ChatbotController.java
│   └── AdminController.java
└── ai/
    ├── FraudDetectionModel.java
    ├── LoanPredictionModel.java
    └── GeminiApiClient.java
```

---

## 3. pom.xml Dependencies

Replace the `<dependencies>` section in `pom.xml` with:

```xml
<dependencies>

    <!-- Spring Boot Core -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>

    <!-- Database -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- Swagger / OpenAPI -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.3.0</version>
    </dependency>

    <!-- HTTP Client for AI API calls -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>

    <!-- Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>

</dependencies>
```

---

## 4. application.properties Configuration

`src/main/resources/application.properties`:

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/fingenie_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# JWT
app.jwt.secret=FinGenieSecretKey2024SuperLongSecretKeyForHMACSHA256Algorithm
app.jwt.expiration=86400000

# Email (for MFA OTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Gemini AI API
app.gemini.api.key=your_gemini_api_key
app.gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent

# Swagger
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

# CORS
app.cors.allowed-origins=http://localhost:3000
```

---

## 5. Entity Layer

### Step 5.1 – Enums

**`enums/Role.java`**
```java
package com.fingenie.enums;

public enum Role {
    CUSTOMER, ADMIN
}
```

**`enums/TransactionType.java`**
```java
package com.fingenie.enums;

public enum TransactionType {
    DEPOSIT, WITHDRAW, TRANSFER, QR_PAYMENT
}
```

**`enums/TransactionStatus.java`**
```java
package com.fingenie.enums;

public enum TransactionStatus {
    SUCCESS, FAILED, FLAGGED, PENDING
}
```

**`enums/LoanStatus.java`**
```java
package com.fingenie.enums;

public enum LoanStatus {
    PENDING, APPROVED, REJECTED
}
```

**`enums/RiskLevel.java`**
```java
package com.fingenie.enums;

public enum RiskLevel {
    LOW, MEDIUM, HIGH
}
```

---

### Step 5.2 – User Entity

**`entity/User.java`**
```java
package com.fingenie.entity;

import com.fingenie.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean mfaEnabled = true;

    private String otpCode;

    private LocalDateTime otpExpiry;

    private boolean verified = false;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Account account;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
```

---

### Step 5.3 – Account Entity

**`entity/Account.java`**
```java
package com.fingenie.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    private String accountType; // SAVINGS, CURRENT

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
```

---

### Step 5.4 – Transaction Entity

**`entity/Transaction.java`**
```java
package com.fingenie.entity;

import com.fingenie.enums.TransactionStatus;
import com.fingenie.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String transactionId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private String description;

    private String category; // AI-categorized: FOOD, TRAVEL, SHOPPING, etc.

    private String receiverAccountNumber;

    private Double riskScore; // AI fraud risk score 0.0 to 1.0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
```

---

### Step 5.5 – FraudAlert Entity

**`entity/FraudAlert.java`**
```java
package com.fingenie.entity;

import com.fingenie.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    private String alertReason;

    private Double riskScore;

    private boolean resolved = false;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
```

---

### Step 5.6 – LoanApplication Entity

**`entity/LoanApplication.java`**
```java
package com.fingenie.entity;

import com.fingenie.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal requestedAmount;

    private Integer tenureMonths;

    private String purpose;

    private Double annualIncome;

    private Integer creditScore;

    private Double existingEmi;

    private Double eligibilityScore; // AI prediction score

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    private String rejectionReason;

    private Double interestRate;

    private Double emiAmount;

    private LocalDateTime appliedAt;

    @PrePersist
    protected void onCreate() {
        appliedAt = LocalDateTime.now();
    }
}
```

---

### Step 5.7 – Investment Entity

**`entity/Investment.java`**
```java
package com.fingenie.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "investments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String fundName;

    private String fundType; // EQUITY, DEBT, HYBRID, SIP

    private BigDecimal investedAmount;

    private BigDecimal currentValue;

    private Double returnPercentage;

    private String riskCategory; // LOW, MEDIUM, HIGH

    private LocalDateTime investedAt;

    @PrePersist
    protected void onCreate() {
        investedAt = LocalDateTime.now();
    }
}
```

---

### Step 5.8 – ChatMessage Entity

**`entity/ChatMessage.java`**
```java
package com.fingenie.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "TEXT")
    private String userMessage;

    @Column(columnDefinition = "TEXT")
    private String botResponse;

    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
```

---

## 6. Repository Layer

### `repository/UserRepository.java`
```java
package com.fingenie.repository;

import com.fingenie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
```

### `repository/AccountRepository.java`
```java
package com.fingenie.repository;

import com.fingenie.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByUserId(Long userId);
}
```

### `repository/TransactionRepository.java`
```java
package com.fingenie.repository;

import com.fingenie.entity.Transaction;
import com.fingenie.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIdOrderByCreatedAtDesc(Long accountId);

    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId AND t.createdAt >= :from")
    List<Transaction> findRecentByAccountId(Long accountId, LocalDateTime from);

    List<Transaction> findByAccountIdAndStatus(Long accountId, TransactionStatus status);

    @Query("SELECT t FROM Transaction t WHERE t.riskScore > :threshold")
    List<Transaction> findHighRiskTransactions(Double threshold);
}
```

### `repository/FraudAlertRepository.java`
```java
package com.fingenie.repository;

import com.fingenie.entity.FraudAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FraudAlertRepository extends JpaRepository<FraudAlert, Long> {
    List<FraudAlert> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<FraudAlert> findByResolved(boolean resolved);
    long countByUserIdAndResolved(Long userId, boolean resolved);
}
```

### `repository/LoanApplicationRepository.java`
```java
package com.fingenie.repository;

import com.fingenie.entity.LoanApplication;
import com.fingenie.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findByUserId(Long userId);
    List<LoanApplication> findByStatus(LoanStatus status);
}
```

### `repository/InvestmentRepository.java`
```java
package com.fingenie.repository;

import com.fingenie.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    List<Investment> findByUserId(Long userId);

    @Query("SELECT SUM(i.investedAmount) FROM Investment i WHERE i.user.id = :userId")
    Double getTotalInvestedByUserId(Long userId);
}
```

### `repository/ChatMessageRepository.java`
```java
package com.fingenie.repository;

import com.fingenie.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByUserIdOrderByTimestampDesc(Long userId);
}
```

---

## 7. DTO Layer

### `dto/request/RegisterRequest.java`
```java
package com.fingenie.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Valid email required")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Pattern(regexp = "^[0-9]{10}$", message = "Valid 10-digit phone required")
    private String phone;
}
```

### `dto/request/LoginRequest.java`
```java
package com.fingenie.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
```

### `dto/request/TransactionRequest.java`
```java
package com.fingenie.dto.request;

import com.fingenie.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionRequest {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.0", message = "Amount must be at least 1")
    private BigDecimal amount;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    private String receiverAccountNumber; // required for TRANSFER

    private String description;
}
```

### `dto/request/LoanRequest.java`
```java
package com.fingenie.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class LoanRequest {

    @NotNull
    @DecimalMin("10000")
    private BigDecimal requestedAmount;

    @NotNull
    @Min(6)
    @Max(360)
    private Integer tenureMonths;

    @NotBlank
    private String purpose;

    @NotNull
    @Min(0)
    private Double annualIncome;

    @NotNull
    @Min(300)
    @Max(900)
    private Integer creditScore;

    private Double existingEmi = 0.0;
}
```

### `dto/request/ChatRequest.java`
```java
package com.fingenie.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRequest {

    @NotBlank(message = "Message cannot be empty")
    private String message;
}
```

### `dto/response/AuthResponse.java`
```java
package com.fingenie.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String email;
    private String fullName;
    private String role;
    private boolean mfaRequired;
    private String message;
}
```

### `dto/response/ApiResponse.java`
```java
package com.fingenie.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
```

### `dto/response/AccountResponse.java`
```java
package com.fingenie.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private String accountType;
    private String ownerName;
    private String ownerEmail;
}
```

### `dto/response/TransactionResponse.java`
```java
package com.fingenie.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private Long id;
    private String transactionId;
    private BigDecimal amount;
    private String type;
    private String status;
    private String description;
    private String category;
    private Double riskScore;
    private LocalDateTime createdAt;
}
```

### `dto/response/LoanResponse.java`
```java
package com.fingenie.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanResponse {
    private Long id;
    private BigDecimal requestedAmount;
    private Integer tenureMonths;
    private String purpose;
    private String status;
    private Double eligibilityScore;
    private Double interestRate;
    private Double emiAmount;
    private String rejectionReason;
}
```

---

## 8. Security – JWT Implementation

### `security/JwtTokenProvider.java`
```java
package com.fingenie.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
```

---

### `security/UserDetailsServiceImpl.java`
```java
package com.fingenie.security;

import com.fingenie.entity.User;
import com.fingenie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
```

---

### `security/JwtAuthFilter.java`
```java
package com.fingenie.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractTokenFromRequest(request);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getEmailFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

---

## 9. Config Classes

### `config/SecurityConfig.java`
```java
package com.fingenie.config;

import com.fingenie.security.JwtAuthFilter;
import com.fingenie.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

---

### `config/CorsConfig.java`
```java
package com.fingenie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(allowedOrigins));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }
}
```

---

## 10. Service Layer

### `service/AuthService.java` (Interface)
```java
package com.fingenie.service;

import com.fingenie.dto.request.*;
import com.fingenie.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse verifyOtp(String email, String otp);
}
```

---

### `serviceImpl/AuthServiceImpl.java`
```java
package com.fingenie.serviceImpl;

import com.fingenie.dto.request.*;
import com.fingenie.dto.response.AuthResponse;
import com.fingenie.entity.*;
import com.fingenie.enums.Role;
import com.fingenie.exception.ResourceNotFoundException;
import com.fingenie.repository.*;
import com.fingenie.security.JwtTokenProvider;
import com.fingenie.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final JavaMailSender mailSender;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(Role.CUSTOMER)
                .mfaEnabled(true)
                .verified(false)
                .build();

        userRepository.save(user);

        // Create bank account
        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(java.math.BigDecimal.ZERO)
                .accountType("SAVINGS")
                .user(user)
                .build();

        accountRepository.save(account);
        sendOtp(user);

        return AuthResponse.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .mfaRequired(true)
                .message("Registration successful. Please verify your email with OTP.")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.isMfaEnabled()) {
            sendOtp(user);
            return AuthResponse.builder()
                    .email(user.getEmail())
                    .mfaRequired(true)
                    .message("OTP sent to your email")
                    .build();
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().name());
        return buildAuthResponse(user, token);
    }

    @Override
    public AuthResponse verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getOtpCode() == null || !user.getOtpCode().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            throw new RuntimeException("OTP expired");
        }

        user.setVerified(true);
        user.setOtpCode(null);
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().name());
        return buildAuthResponse(user, token);
    }

    private void sendOtp(User user) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setOtpCode(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("FinGenie – Your OTP Code");
        message.setText("Your OTP is: " + otp + "\nValid for 10 minutes.");
        mailSender.send(message);
    }

    private AuthResponse buildAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .mfaRequired(false)
                .message("Login successful")
                .build();
    }

    private String generateAccountNumber() {
        return "FG" + System.currentTimeMillis();
    }
}
```

---

### `serviceImpl/TransactionServiceImpl.java`
```java
package com.fingenie.serviceImpl;

import com.fingenie.ai.FraudDetectionModel;
import com.fingenie.dto.request.TransactionRequest;
import com.fingenie.dto.response.TransactionResponse;
import com.fingenie.entity.*;
import com.fingenie.enums.*;
import com.fingenie.exception.*;
import com.fingenie.repository.*;
import com.fingenie.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final FraudAlertRepository fraudAlertRepository;
    private final FraudDetectionModel fraudDetectionModel;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TransactionResponse processTransaction(String userEmail, TransactionRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Account account = accountRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        // Fraud detection
        double riskScore = fraudDetectionModel.calculateRiskScore(account, request.getAmount());

        if (riskScore > 0.85) {
            // Flag high-risk transaction
            Transaction flagged = Transaction.builder()
                    .transactionId(UUID.randomUUID().toString())
                    .amount(request.getAmount())
                    .type(request.getType())
                    .status(TransactionStatus.FLAGGED)
                    .description(request.getDescription())
                    .riskScore(riskScore)
                    .account(account)
                    .build();
            transactionRepository.save(flagged);

            FraudAlert alert = FraudAlert.builder()
                    .transaction(flagged)
                    .user(user)
                    .riskLevel(RiskLevel.HIGH)
                    .riskScore(riskScore)
                    .alertReason("Unusual transaction amount or pattern detected")
                    .build();
            fraudAlertRepository.save(alert);

            throw new FraudDetectedException("Transaction flagged for suspicious activity");
        }

        // Process transaction
        switch (request.getType()) {
            case DEPOSIT -> account.setBalance(account.getBalance().add(request.getAmount()));
            case WITHDRAW -> {
                if (account.getBalance().compareTo(request.getAmount()) < 0)
                    throw new InsufficientFundsException("Insufficient funds");
                account.setBalance(account.getBalance().subtract(request.getAmount()));
            }
            case TRANSFER -> {
                if (account.getBalance().compareTo(request.getAmount()) < 0)
                    throw new InsufficientFundsException("Insufficient funds");
                Account receiver = accountRepository.findByAccountNumber(request.getReceiverAccountNumber())
                        .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));
                account.setBalance(account.getBalance().subtract(request.getAmount()));
                receiver.setBalance(receiver.getBalance().add(request.getAmount()));
                accountRepository.save(receiver);
            }
        }

        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .amount(request.getAmount())
                .type(request.getType())
                .status(TransactionStatus.SUCCESS)
                .description(request.getDescription())
                .receiverAccountNumber(request.getReceiverAccountNumber())
                .riskScore(riskScore)
                .category(categorizeTransaction(request.getDescription()))
                .account(account)
                .build();

        transactionRepository.save(transaction);
        return mapToResponse(transaction);
    }

    @Override
    public List<TransactionResponse> getTransactionHistory(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Account account = accountRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return transactionRepository.findByAccountIdOrderByCreatedAtDesc(account.getId())
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private String categorizeTransaction(String description) {
        if (description == null) return "GENERAL";
        String d = description.toLowerCase();
        if (d.contains("food") || d.contains("restaurant") || d.contains("swiggy")) return "FOOD";
        if (d.contains("uber") || d.contains("ola") || d.contains("travel")) return "TRAVEL";
        if (d.contains("amazon") || d.contains("flipkart") || d.contains("shop")) return "SHOPPING";
        if (d.contains("netflix") || d.contains("spotify")) return "ENTERTAINMENT";
        return "GENERAL";
    }

    private TransactionResponse mapToResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .transactionId(t.getTransactionId())
                .amount(t.getAmount())
                .type(t.getType().name())
                .status(t.getStatus().name())
                .description(t.getDescription())
                .category(t.getCategory())
                .riskScore(t.getRiskScore())
                .createdAt(t.getCreatedAt())
                .build();
    }
}
```

---

### `serviceImpl/LoanServiceImpl.java`
```java
package com.fingenie.serviceImpl;

import com.fingenie.ai.LoanPredictionModel;
import com.fingenie.dto.request.LoanRequest;
import com.fingenie.dto.response.LoanResponse;
import com.fingenie.entity.*;
import com.fingenie.enums.LoanStatus;
import com.fingenie.exception.ResourceNotFoundException;
import com.fingenie.repository.*;
import com.fingenie.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanApplicationRepository loanRepository;
    private final UserRepository userRepository;
    private final LoanPredictionModel loanPredictionModel;

    @Override
    public LoanResponse applyForLoan(String userEmail, LoanRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        double eligibilityScore = loanPredictionModel.predict(
                request.getCreditScore(),
                request.getAnnualIncome(),
                request.getRequestedAmount().doubleValue(),
                request.getExistingEmi()
        );

        LoanStatus status = eligibilityScore >= 0.6 ? LoanStatus.APPROVED : LoanStatus.REJECTED;
        double interestRate = calculateInterestRate(request.getCreditScore());
        double emi = calculateEmi(request.getRequestedAmount().doubleValue(), interestRate, request.getTenureMonths());

        LoanApplication loan = LoanApplication.builder()
                .user(user)
                .requestedAmount(request.getRequestedAmount())
                .tenureMonths(request.getTenureMonths())
                .purpose(request.getPurpose())
                .annualIncome(request.getAnnualIncome())
                .creditScore(request.getCreditScore())
                .existingEmi(request.getExistingEmi())
                .eligibilityScore(eligibilityScore)
                .status(status)
                .interestRate(interestRate)
                .emiAmount(emi)
                .rejectionReason(status == LoanStatus.REJECTED ? "Low eligibility score based on credit profile" : null)
                .build();

        loanRepository.save(loan);

        return LoanResponse.builder()
                .id(loan.getId())
                .requestedAmount(loan.getRequestedAmount())
                .tenureMonths(loan.getTenureMonths())
                .purpose(loan.getPurpose())
                .status(loan.getStatus().name())
                .eligibilityScore(eligibilityScore)
                .interestRate(interestRate)
                .emiAmount(emi)
                .rejectionReason(loan.getRejectionReason())
                .build();
    }

    @Override
    public List<LoanResponse> getLoansByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return loanRepository.findByUserId(user.getId())
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public double calculateEmi(double principal, double annualRate, int months) {
        double monthlyRate = annualRate / 12 / 100;
        return (principal * monthlyRate * Math.pow(1 + monthlyRate, months)) /
               (Math.pow(1 + monthlyRate, months) - 1);
    }

    private double calculateInterestRate(int creditScore) {
        if (creditScore >= 750) return 8.5;
        if (creditScore >= 700) return 10.5;
        if (creditScore >= 650) return 13.0;
        return 16.0;
    }

    private LoanResponse mapToResponse(LoanApplication loan) {
        return LoanResponse.builder()
                .id(loan.getId())
                .requestedAmount(loan.getRequestedAmount())
                .tenureMonths(loan.getTenureMonths())
                .purpose(loan.getPurpose())
                .status(loan.getStatus().name())
                .eligibilityScore(loan.getEligibilityScore())
                .interestRate(loan.getInterestRate())
                .emiAmount(loan.getEmiAmount())
                .build();
    }
}
```

---

## 11. Controller Layer

### `controller/AuthController.java`
```java
package com.fingenie.controller;

import com.fingenie.dto.request.*;
import com.fingenie.dto.response.*;
import com.fingenie.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register, Login, OTP Verification")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", response));
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP for MFA")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp) {
        AuthResponse response = authService.verifyOtp(email, otp);
        return ResponseEntity.ok(ApiResponse.success("OTP verified successfully", response));
    }
}
```

---

### `controller/AccountController.java`
```java
package com.fingenie.controller;

import com.fingenie.dto.response.*;
import com.fingenie.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Account information and balance")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AccountResponse>> getMyAccount(
            @AuthenticationPrincipal UserDetails userDetails) {
        AccountResponse account = accountService.getAccountByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Account retrieved", account));
    }
}
```

---

### `controller/TransactionController.java`
```java
package com.fingenie.controller;

import com.fingenie.dto.request.TransactionRequest;
import com.fingenie.dto.response.*;
import com.fingenie.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Deposit, Withdraw, Transfer")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/process")
    public ResponseEntity<ApiResponse<TransactionResponse>> process(
            @Valid @RequestBody TransactionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        TransactionResponse response = transactionService.processTransaction(
                userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Transaction successful", response));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getHistory(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<TransactionResponse> history = transactionService.getTransactionHistory(
                userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Transaction history retrieved", history));
    }
}
```

---

### `controller/LoanController.java`
```java
package com.fingenie.controller;

import com.fingenie.dto.request.LoanRequest;
import com.fingenie.dto.response.*;
import com.fingenie.service.LoanService;
import com.fingenie.serviceImpl.LoanServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Tag(name = "Loan Management", description = "Apply for loans, EMI calculator, AI eligibility")
public class LoanController {

    private final LoanService loanService;
    private final LoanServiceImpl loanServiceImpl;

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<LoanResponse>> apply(
            @Valid @RequestBody LoanRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        LoanResponse response = loanService.applyForLoan(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Loan application submitted", response));
    }

    @GetMapping("/my-loans")
    public ResponseEntity<ApiResponse<List<LoanResponse>>> getMyLoans(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<LoanResponse> loans = loanService.getLoansByUser(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Loans retrieved", loans));
    }

    @GetMapping("/emi-calculator")
    public ResponseEntity<ApiResponse<Map<String, Double>>> calculateEmi(
            @RequestParam double principal,
            @RequestParam double annualRate,
            @RequestParam int months) {
        double emi = loanServiceImpl.calculateEmi(principal, annualRate, months);
        return ResponseEntity.ok(ApiResponse.success("EMI calculated",
                Map.of("emi", emi, "totalPayment", emi * months,
                       "totalInterest", (emi * months) - principal)));
    }
}
```

---

### `controller/FraudController.java`
```java
package com.fingenie.controller;

import com.fingenie.dto.response.*;
import com.fingenie.entity.FraudAlert;
import com.fingenie.exception.ResourceNotFoundException;
import com.fingenie.repository.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fraud")
@RequiredArgsConstructor
@Tag(name = "Fraud Detection", description = "Fraud alerts and risk scores")
public class FraudController {

    private final FraudAlertRepository fraudAlertRepository;
    private final UserRepository userRepository;

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<FraudAlertResponse>>> getMyAlerts(
            @AuthenticationPrincipal UserDetails userDetails) {
        var user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<FraudAlertResponse> alerts = fraudAlertRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("Alerts retrieved", alerts));
    }

    private FraudAlertResponse mapToResponse(FraudAlert alert) {
        return FraudAlertResponse.builder()
                .id(alert.getId())
                .riskLevel(alert.getRiskLevel().name())
                .alertReason(alert.getAlertReason())
                .riskScore(alert.getRiskScore())
                .resolved(alert.isResolved())
                .createdAt(alert.getCreatedAt())
                .build();
    }
}
```

---

### `controller/ChatbotController.java`
```java
package com.fingenie.controller;

import com.fingenie.dto.request.ChatRequest;
import com.fingenie.dto.response.*;
import com.fingenie.service.ChatbotService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "AI Chatbot", description = "NLP banking assistant powered by Gemini AI")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/message")
    public ResponseEntity<ApiResponse<ChatResponse>> sendMessage(
            @Valid @RequestBody ChatRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        ChatResponse response = chatbotService.chat(userDetails.getUsername(), request.getMessage());
        return ResponseEntity.ok(ApiResponse.success("Response received", response));
    }
}
```

---

### `controller/AdminController.java`
```java
package com.fingenie.controller;

import com.fingenie.dto.response.ApiResponse;
import com.fingenie.repository.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Admin analytics and management")
public class AdminController {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final FraudAlertRepository fraudAlertRepository;
    private final LoanApplicationRepository loanRepository;

    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAnalytics() {
        Map<String, Object> analytics = Map.of(
                "totalUsers", userRepository.count(),
                "totalTransactions", transactionRepository.count(),
                "unresolvedFraudAlerts", fraudAlertRepository.findByResolved(false).size(),
                "totalLoanApplications", loanRepository.count()
        );
        return ResponseEntity.ok(ApiResponse.success("Analytics retrieved", analytics));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<?>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success("Users retrieved", userRepository.findAll()));
    }

    @GetMapping("/fraud-reports")
    public ResponseEntity<ApiResponse<?>> getAllFraudAlerts() {
        return ResponseEntity.ok(ApiResponse.success("Fraud reports retrieved",
                fraudAlertRepository.findAll()));
    }
}
```

---

## 12. AI Integration

### `ai/FraudDetectionModel.java`
```java
package com.fingenie.ai;

import com.fingenie.entity.Account;
import com.fingenie.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FraudDetectionModel {

    private final TransactionRepository transactionRepository;

    /**
     * Rule-based + statistical fraud scoring
     * Returns a risk score between 0.0 (safe) and 1.0 (high risk)
     */
    public double calculateRiskScore(Account account, BigDecimal amount) {
        double score = 0.0;

        // Rule 1: Amount exceeds 50% of current balance
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            double ratio = amount.doubleValue() / account.getBalance().doubleValue();
            if (ratio > 0.5) score += 0.3;
            if (ratio > 0.9) score += 0.3;
        }

        // Rule 2: Multiple transactions in last 5 minutes
        List<?> recentTx = transactionRepository.findRecentByAccountId(
                account.getId(), LocalDateTime.now().minusMinutes(5));
        if (recentTx.size() > 3) score += 0.2;
        if (recentTx.size() > 5) score += 0.2;

        // Rule 3: Very large amount
        if (amount.compareTo(new BigDecimal("100000")) > 0) score += 0.2;
        if (amount.compareTo(new BigDecimal("500000")) > 0) score += 0.2;

        return Math.min(score, 1.0);
    }
}
```

---

### `ai/LoanPredictionModel.java`
```java
package com.fingenie.ai;

import org.springframework.stereotype.Component;

@Component
public class LoanPredictionModel {

    /**
     * Weighted scoring model for loan eligibility
     * Returns a score between 0.0 (not eligible) and 1.0 (highly eligible)
     */
    public double predict(int creditScore, double annualIncome,
                          double requestedAmount, double existingEmi) {
        double score = 0.0;

        // Credit score weight (40%)
        if (creditScore >= 750) score += 0.40;
        else if (creditScore >= 700) score += 0.30;
        else if (creditScore >= 650) score += 0.20;
        else if (creditScore >= 600) score += 0.10;

        // Income-to-loan ratio weight (35%)
        double incomeRatio = annualIncome / requestedAmount;
        if (incomeRatio >= 3.0) score += 0.35;
        else if (incomeRatio >= 2.0) score += 0.25;
        else if (incomeRatio >= 1.5) score += 0.15;

        // Debt-to-income ratio weight (25%)
        double monthlyIncome = annualIncome / 12;
        double debtRatio = existingEmi / monthlyIncome;
        if (debtRatio <= 0.2) score += 0.25;
        else if (debtRatio <= 0.35) score += 0.15;
        else if (debtRatio <= 0.5) score += 0.05;

        return Math.min(score, 1.0);
    }
}
```

---

### `ai/GeminiApiClient.java`
```java
package com.fingenie.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class GeminiApiClient {

    @Value("${app.gemini.api.key}")
    private String apiKey;

    @Value("${app.gemini.api.url}")
    private String apiUrl;

    private final WebClient webClient;

    public GeminiApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String askGemini(String prompt) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(Map.of("text", prompt)))
                    )
            );

            Map<?, ?> response = webClient.post()
                    .uri(apiUrl + "?key=" + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("candidates")) {
                List<?> candidates = (List<?>) response.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<?, ?> content = (Map<?, ?>) ((Map<?, ?>) candidates.get(0)).get("content");
                    List<?> parts = (List<?>) content.get("parts");
                    return (String) ((Map<?, ?>) parts.get(0)).get("text");
                }
            }
        } catch (Exception e) {
            return "I'm sorry, I couldn't process your request at the moment. Please try again.";
        }
        return "No response from AI.";
    }
}
```

---

### `serviceImpl/ChatbotServiceImpl.java`
```java
package com.fingenie.serviceImpl;

import com.fingenie.ai.GeminiApiClient;
import com.fingenie.dto.response.ChatResponse;
import com.fingenie.entity.*;
import com.fingenie.exception.ResourceNotFoundException;
import com.fingenie.repository.*;
import com.fingenie.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

    private final GeminiApiClient geminiApiClient;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    public ChatResponse chat(String userEmail, String message) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Build banking context for AI
        String contextPrompt = buildBankingPrompt(user, message);
        String botReply = geminiApiClient.askGemini(contextPrompt);

        // Save chat history
        ChatMessage chatMessage = ChatMessage.builder()
                .user(user)
                .userMessage(message)
                .botResponse(botReply)
                .build();
        chatMessageRepository.save(chatMessage);

        return ChatResponse.builder()
                .message(botReply)
                .timestamp(chatMessage.getTimestamp())
                .build();
    }

    private String buildBankingPrompt(User user, String message) {
        return "You are FinGenie, an intelligent banking assistant for " + user.getFullName() +
               ". Answer only banking-related questions about accounts, transactions, loans, " +
               "investments, and financial advice. Be concise and helpful. " +
               "User question: " + message;
    }
}
```

---

## 13. Exception Handling

### `exception/GlobalExceptionHandler.java`
```java
package com.fingenie.exception;

import com.fingenie.dto.response.ApiResponse;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleNotFound(ResourceNotFoundException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleInsufficientFunds(InsufficientFundsException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(FraudDetectedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<?> handleFraud(FraudDetectedException ex) {
        return ApiResponse.error("FRAUD_ALERT: " + ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleBadCredentials(BadCredentialsException ex) {
        return ApiResponse.error("Invalid email or password");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<?> handleAccessDenied(AccessDeniedException ex) {
        return ApiResponse.error("Access denied");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            errors.put(field, error.getDefaultMessage());
        });
        return new ApiResponse<>(false, "Validation failed", errors);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleRuntime(RuntimeException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleGeneral(Exception ex) {
        return ApiResponse.error("An internal server error occurred");
    }
}
```

### Custom Exceptions
```java
// ResourceNotFoundException.java
package com.fingenie.exception;
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) { super(message); }
}

// InsufficientFundsException.java
package com.fingenie.exception;
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) { super(message); }
}

// FraudDetectedException.java
package com.fingenie.exception;
public class FraudDetectedException extends RuntimeException {
    public FraudDetectedException(String message) { super(message); }
}
```

---

## 14. Swagger Configuration

### `config/SwaggerConfig.java`
```java
package com.fingenie.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.*;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI fingernieOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FinGenie AI – Banking API")
                        .description("Intelligent Digital Banking & Wealth Management Platform")
                        .version("1.0.0")
                        .contact(new Contact().name("FinGenie Team")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
```

Access Swagger at: `http://localhost:8080/swagger-ui.html`

---

## 15. Database Schema (MySQL)

Run this in MySQL Workbench or execute via Spring JPA auto-create:

```sql
CREATE DATABASE IF NOT EXISTS fingenie_db;
USE fingenie_db;

-- Tables are auto-created by Spring JPA (ddl-auto=update)
-- Verify with:
SHOW TABLES;

-- Expected tables:
-- users
-- accounts
-- transactions
-- fraud_alerts
-- loan_applications
-- investments
-- chat_messages

-- Create admin user manually (after app starts):
INSERT INTO users (full_name, email, password, phone, role, mfa_enabled, verified, created_at)
VALUES (
    'Admin User',
    'admin@fingenie.com',
    '$2a$10$...', -- BCrypt encoded password
    '9999999999',
    'ADMIN',
    0,
    1,
    NOW()
);
```

---

## 16. Run & Test

### Step 16.1 – Run the Application

In STS:
1. Right-click `FingenieBackendApplication.java`
2. Select `Run As → Spring Boot App`
3. Check console for: `Started FingenieBackendApplication on port 8080`

---

### Step 16.2 – Test with Postman

**Register:**
```
POST http://localhost:8080/api/auth/register
Body (JSON):
{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "9876543210"
}
```

**Verify OTP:**
```
POST http://localhost:8080/api/auth/verify-otp?email=john@example.com&otp=123456
```

**Login:**
```
POST http://localhost:8080/api/auth/login
Body (JSON):
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Use Token for Protected Routes:**
```
Header: Authorization: Bearer <token_from_login>

GET http://localhost:8080/api/accounts/me
```

**Process Transaction:**
```
POST http://localhost:8080/api/transactions/process
Authorization: Bearer <token>
Body:
{
  "amount": 5000,
  "type": "DEPOSIT",
  "description": "Salary credit"
}
```

**Apply for Loan:**
```
POST http://localhost:8080/api/loans/apply
Authorization: Bearer <token>
Body:
{
  "requestedAmount": 500000,
  "tenureMonths": 60,
  "purpose": "Home renovation",
  "annualIncome": 800000,
  "creditScore": 720,
  "existingEmi": 5000
}
```

**Chat with AI:**
```
POST http://localhost:8080/api/chat/message
Authorization: Bearer <token>
Body:
{
  "message": "What is my current balance and any investment suggestions?"
}
```

---

### Step 16.3 – Swagger UI

Open browser: `http://localhost:8080/swagger-ui.html`

1. Click **Authorize** button (top right)
2. Enter: `Bearer <your_jwt_token>`
3. All APIs are now testable from the UI

---

### Step 16.4 – Mapping to Rubric Marks

| Rubric Criterion | Implementation |
|---|---|
| React UI (10) | Frontend connects to these APIs |
| React State Management (10) | Auth token stored, used in all requests |
| Spring Boot REST APIs (15) | 6 controllers, 20+ endpoints |
| Banking Transaction Logic (10) | `TransactionServiceImpl` – deposit/withdraw/transfer |
| AI Fraud Detection (10) | `FraudDetectionModel` + `FraudAlert` entity |
| AI Loan Prediction (10) | `LoanPredictionModel` – weighted scoring |
| JWT Security (10) | `JwtTokenProvider` + `JwtAuthFilter` + `SecurityConfig` |
| Database Design (10) | 7 entities with relationships |
| Frontend-Backend Integration (10) | CORS config + consistent `ApiResponse<T>` wrapper |
| Exception Handling & Validation (5) | `GlobalExceptionHandler` + Bean Validation |
| Documentation & Deployment (5) | Swagger + `application.properties` profiles |

---

*FinGenie AI Backend – Complete Implementation Guide*
*Spring Boot 3.2 | Java 17 | MySQL | JWT | Gemini AI*
