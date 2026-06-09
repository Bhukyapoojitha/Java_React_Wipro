# FinGenie Backend – Missing Code Files (Based on Project Structure)

This file contains ONLY the missing components required to complete the project structure provided in the uploaded FinGenie backend guide.

---

# 1. MAIN APPLICATION CLASS (if not already created)

```java
package com.fingenie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FingenieBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(FingenieBackendApplication.class, args);
    }
}
```

---

# 2. MISSING DTOs

## FraudAlertResponse.java
```java
package com.fingenie.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
public class FraudAlertResponse {
    private Long id;
    private String riskLevel;
    private String alertReason;
    private Double riskScore;
    private boolean resolved;
    private LocalDateTime createdAt;
}
```

## ChatResponse.java
```java
package com.fingenie.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
public class ChatResponse {
    private String message;
    private LocalDateTime timestamp;
}
```

---

# 3. MISSING SERVICE INTERFACES

## AccountService.java
```java
package com.fingenie.service;

import com.fingenie.dto.response.AccountResponse;

public interface AccountService {
    AccountResponse getAccountByEmail(String email);
}
```

## TransactionService.java
```java
package com.fingenie.service;

import com.fingenie.dto.request.TransactionRequest;
import com.fingenie.dto.response.TransactionResponse;
import java.util.List;

public interface TransactionService {
    TransactionResponse processTransaction(String email, TransactionRequest request);
    List<TransactionResponse> getTransactionHistory(String email);
}
```

## LoanService.java
```java
package com.fingenie.service;

import com.fingenie.dto.request.LoanRequest;
import com.fingenie.dto.response.LoanResponse;
import java.util.List;

public interface LoanService {
    LoanResponse applyForLoan(String email, LoanRequest request);
    List<LoanResponse> getLoansByUser(String email);
}
```

## ChatbotService.java
```java
package com.fingenie.service;

import com.fingenie.dto.response.ChatResponse;

public interface ChatbotService {
    ChatResponse chat(String email, String message);
}
```

---

# 4. MISSING SERVICE IMPLEMENTATION

## AccountServiceImpl.java
```java
package com.fingenie.serviceImpl;

import com.fingenie.dto.response.AccountResponse;
import com.fingenie.entity.Account;
import com.fingenie.entity.User;
import com.fingenie.exception.ResourceNotFoundException;
import com.fingenie.repository.AccountRepository;
import com.fingenie.repository.UserRepository;
import com.fingenie.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    public AccountResponse getAccountByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Account account = accountRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .ownerName(user.getFullName())
                .ownerEmail(user.getEmail())
                .build();
    }
}
```

---

# 5. MISSING CONFIGURATION

## WebClientConfig.java
```java
package com.fingenie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
```

---

# 6. REQUIRED FIX

## TransactionRepository fix
```java
@Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId AND t.createdAt >= :from")
List<Transaction> findRecentByAccountId(
        @org.springframework.data.repository.query.Param("accountId") Long accountId,
        @org.springframework.data.repository.query.Param("from") java.time.LocalDateTime from);
```

---

# END OF FILE
