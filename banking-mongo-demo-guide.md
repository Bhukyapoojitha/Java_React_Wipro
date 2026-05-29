# Banking Management System — Spring Boot + MongoDB (Embedded Documents)

> Step-by-step guide to build a Banking Management System using Spring Boot,
> Spring Data MongoDB (embedded documents), and Swagger UI for testing.
>
> Entities: **Account** (root document) + **Transaction** (embedded)

---

## Prerequisites

Before opening STS, ensure you have:

- **STS 4** — https://spring.io/tools
- **Java 17+** — verify with `java -version`
- **MongoDB** — https://www.mongodb.com/try/download/community

  Or run via Docker:
  ```bash
  docker run -d -p 27017:27017 --name mongo mongo:7
  ```
- **Maven** — bundled with STS

---

## Step 1 — Create the Spring Boot Project in STS

1. Open STS → **File → New → Spring Starter Project**
2. Fill in the dialog:

   | Field        | Value                  |
   |--------------|------------------------|
   | Name         | `banking-mongo-demo`   |
   | Type         | Maven                  |
   | Java Version | 17                     |
   | Packaging    | Jar                    |
   | Group        | `com.banking`          |
   | Artifact     | `banking-mongo-demo`   |

3. Click **Next** → search and add:
   - Spring Data MongoDB
   - Spring Web
   - Lombok
   - Spring Boot DevTools

4. Click **Finish**

---

## Step 2 — Add Swagger Dependency to pom.xml

Open `pom.xml` and add inside `<dependencies>`:

```xml
<!-- Swagger UI via SpringDoc -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

Save — STS will auto-download the dependency.

---

## Step 3 — Configure application.properties

File: `src/main/resources/application.properties`

```properties
# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/bankingdb
spring.data.mongodb.auto-index-creation=true

# App name
spring.application.name=Banking Management System

# Swagger UI path
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs
```

---

## Step 4 — Create the Domain Model

### 4a. TransactionType.java (enum)

File: `src/main/java/com/banking/bankingmongodemo/model/TransactionType.java`

```java
package com.banking.bankingmongodemo.model;

public enum TransactionType {
    DEPOSIT, WITHDRAWAL, TRANSFER
}
```

### 4b. Transaction.java — embedded subdocument (no @Document)

File: `src/main/java/com/banking/bankingmongodemo/model/Transaction.java`

```java
package com.banking.bankingmongodemo.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Transaction {
    private String transactionId;
    private TransactionType type;
    private BigDecimal amount;
    private String description;
    private LocalDateTime timestamp;
    private BigDecimal balanceAfter;   // snapshot of balance after this txn
}
```

### 4c. Account.java — root @Document

File: `src/main/java/com/banking/bankingmongodemo/model/Account.java`

```java
package com.banking.bankingmongodemo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "accounts")
public class Account {

    @Id
    private String id;

    @Indexed(unique = true)
    private String accountNumber;

    private String ownerName;
    private String email;
    private String accountType;        // SAVINGS, CURRENT
    private BigDecimal balance;
    private LocalDateTime createdAt;

    @Version                           // optimistic locking
    private Long version;

    private List<Transaction> transactions = new ArrayList<>();
}
```

---

## Step 5 — Create Request DTOs

### CreateAccountRequest.java

File: `src/main/java/com/banking/bankingmongodemo/dto/CreateAccountRequest.java`

```java
package com.banking.bankingmongodemo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "Request body to create a new bank account")
public class CreateAccountRequest {

    @Schema(example = "John Doe", required = true)
    private String ownerName;

    @Schema(example = "john@example.com", required = true)
    private String email;

    @Schema(example = "SAVINGS", allowableValues = {"SAVINGS", "CURRENT"})
    private String accountType;

    @Schema(example = "1000.00", description = "Initial deposit amount")
    private BigDecimal initialDeposit;
}
```

### TransactionRequest.java

File: `src/main/java/com/banking/bankingmongodemo/dto/TransactionRequest.java`

```java
package com.banking.bankingmongodemo.dto;

import com.banking.bankingmongodemo.model.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "Request body to perform a transaction")
public class TransactionRequest {

    @Schema(example = "DEPOSIT", required = true)
    private TransactionType type;

    @Schema(example = "500.00", required = true)
    private BigDecimal amount;

    @Schema(example = "Salary credited")
    private String description;

    @Schema(example = "ACC-0002", description = "Required only for TRANSFER type")
    private String targetAccountNumber;
}
```

---

## Step 6 — Create the Repository

File: `src/main/java/com/banking/bankingmongodemo/repository/AccountRepository.java`

```java
package com.banking.bankingmongodemo.repository;

import com.banking.bankingmongodemo.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String> {

    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByEmail(String email);

    List<Account> findByAccountType(String accountType);

    // Query into embedded transaction array using dot notation
    @Query("{ 'transactions.transactionId': ?0 }")
    Optional<Account> findByTransactionId(String transactionId);

    @Query("{ 'transactions.type': ?0 }")
    List<Account> findAccountsWithTransactionType(String type);
}
```

---

## Step 7 — Create the Service

File: `src/main/java/com/banking/bankingmongodemo/service/BankingService.java`

```java
package com.banking.bankingmongodemo.service;

import com.banking.bankingmongodemo.dto.CreateAccountRequest;
import com.banking.bankingmongodemo.dto.TransactionRequest;
import com.banking.bankingmongodemo.model.Account;
import com.banking.bankingmongodemo.model.Transaction;
import com.banking.bankingmongodemo.model.TransactionType;
import com.banking.bankingmongodemo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankingService {

    private final AccountRepository accountRepo;

    // ── Create Account ────────────────────────────────────────────────────────

    public Account createAccount(CreateAccountRequest req) {
        String accNumber = "ACC-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Account account = new Account();
        account.setAccountNumber(accNumber);
        account.setOwnerName(req.getOwnerName());
        account.setEmail(req.getEmail());
        account.setAccountType(req.getAccountType() != null ? req.getAccountType() : "SAVINGS");
        account.setBalance(req.getInitialDeposit() != null ? req.getInitialDeposit() : BigDecimal.ZERO);
        account.setCreatedAt(LocalDateTime.now());

        // Record initial deposit as first transaction
        if (req.getInitialDeposit() != null && req.getInitialDeposit().compareTo(BigDecimal.ZERO) > 0) {
            Transaction t = buildTransaction(
                TransactionType.DEPOSIT, req.getInitialDeposit(),
                "Initial deposit", account.getBalance()
            );
            account.getTransactions().add(t);
        }

        return accountRepo.save(account);
    }

    // ── Deposit ───────────────────────────────────────────────────────────────

    public Account deposit(String accountNumber, TransactionRequest req) {
        Account account = findOrThrow(accountNumber);

        account.setBalance(account.getBalance().add(req.getAmount()));
        account.getTransactions().add(
            buildTransaction(TransactionType.DEPOSIT, req.getAmount(),
                req.getDescription(), account.getBalance())
        );

        return accountRepo.save(account);
    }

    // ── Withdraw ──────────────────────────────────────────────────────────────

    public Account withdraw(String accountNumber, TransactionRequest req) {
        Account account = findOrThrow(accountNumber);

        if (account.getBalance().compareTo(req.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance. Current balance: " + account.getBalance());
        }

        account.setBalance(account.getBalance().subtract(req.getAmount()));
        account.getTransactions().add(
            buildTransaction(TransactionType.WITHDRAWAL, req.getAmount(),
                req.getDescription(), account.getBalance())
        );

        return accountRepo.save(account);
    }

    // ── Transfer ──────────────────────────────────────────────────────────────

    public String transfer(String fromAccountNumber, TransactionRequest req) {
        Account from = findOrThrow(fromAccountNumber);
        Account to   = findOrThrow(req.getTargetAccountNumber());

        if (from.getBalance().compareTo(req.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance for transfer.");
        }

        from.setBalance(from.getBalance().subtract(req.getAmount()));
        from.getTransactions().add(
            buildTransaction(TransactionType.TRANSFER, req.getAmount(),
                "Transfer to " + to.getAccountNumber(), from.getBalance())
        );

        to.setBalance(to.getBalance().add(req.getAmount()));
        to.getTransactions().add(
            buildTransaction(TransactionType.TRANSFER, req.getAmount(),
                "Transfer from " + from.getAccountNumber(), to.getBalance())
        );

        accountRepo.save(from);
        accountRepo.save(to);

        return String.format("Transferred %.2f from %s to %s",
            req.getAmount(), fromAccountNumber, req.getTargetAccountNumber());
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    public List<Account> getAllAccounts()                { return accountRepo.findAll(); }
    public Account getByAccountNumber(String num)        { return findOrThrow(num); }
    public List<Transaction> getTransactions(String num) { return findOrThrow(num).getTransactions(); }
    public List<Account> getByType(String type)          { return accountRepo.findByAccountType(type); }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Account findOrThrow(String accountNumber) {
        return accountRepo.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
    }

    private Transaction buildTransaction(TransactionType type, BigDecimal amount,
                                         String description, BigDecimal balanceAfter) {
        Transaction t = new Transaction();
        t.setTransactionId(UUID.randomUUID().toString());
        t.setType(type);
        t.setAmount(amount);
        t.setDescription(description);
        t.setTimestamp(LocalDateTime.now());
        t.setBalanceAfter(balanceAfter);
        return t;
    }
}
```

---

## Step 8 — Create the REST Controller with Swagger Annotations

File: `src/main/java/com/banking/bankingmongodemo/controller/BankingController.java`

```java
package com.banking.bankingmongodemo.controller;

import com.banking.bankingmongodemo.dto.CreateAccountRequest;
import com.banking.bankingmongodemo.dto.TransactionRequest;
import com.banking.bankingmongodemo.model.Account;
import com.banking.bankingmongodemo.model.Transaction;
import com.banking.bankingmongodemo.service.BankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banking")
@RequiredArgsConstructor
@Tag(name = "Banking API", description = "Account and Transaction management")
public class BankingController {

    private final BankingService bankingService;

    // ── Account Endpoints ─────────────────────────────────────────────────────

    @PostMapping("/accounts")
    @Operation(summary = "Create a new bank account")
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequest req) {
        return ResponseEntity.ok(bankingService.createAccount(req));
    }

    @GetMapping("/accounts")
    @Operation(summary = "Get all accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(bankingService.getAllAccounts());
    }

    @GetMapping("/accounts/{accountNumber}")
    @Operation(summary = "Get account by account number")
    public ResponseEntity<Account> getAccount(
            @Parameter(description = "Account number e.g. ACC-ABC123")
            @PathVariable String accountNumber) {
        return ResponseEntity.ok(bankingService.getByAccountNumber(accountNumber));
    }

    @GetMapping("/accounts/type/{type}")
    @Operation(summary = "Get accounts by type (SAVINGS or CURRENT)")
    public ResponseEntity<List<Account>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(bankingService.getByType(type));
    }

    // ── Transaction Endpoints ─────────────────────────────────────────────────

    @PostMapping("/accounts/{accountNumber}/deposit")
    @Operation(summary = "Deposit money into an account")
    public ResponseEntity<Account> deposit(
            @PathVariable String accountNumber,
            @RequestBody TransactionRequest req) {
        return ResponseEntity.ok(bankingService.deposit(accountNumber, req));
    }

    @PostMapping("/accounts/{accountNumber}/withdraw")
    @Operation(summary = "Withdraw money from an account")
    public ResponseEntity<Account> withdraw(
            @PathVariable String accountNumber,
            @RequestBody TransactionRequest req) {
        return ResponseEntity.ok(bankingService.withdraw(accountNumber, req));
    }

    @PostMapping("/accounts/{accountNumber}/transfer")
    @Operation(summary = "Transfer money to another account")
    public ResponseEntity<String> transfer(
            @PathVariable String accountNumber,
            @RequestBody TransactionRequest req) {
        return ResponseEntity.ok(bankingService.transfer(accountNumber, req));
    }

    @GetMapping("/accounts/{accountNumber}/transactions")
    @Operation(summary = "Get all transactions for an account")
    public ResponseEntity<List<Transaction>> getTransactions(
            @PathVariable String accountNumber) {
        return ResponseEntity.ok(bankingService.getTransactions(accountNumber));
    }
}
```

---

## Step 9 — Add Swagger Config and Data Seeder

### SwaggerConfig.java

File: `src/main/java/com/banking/bankingmongodemo/config/SwaggerConfig.java`

```java
package com.banking.bankingmongodemo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bankingOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Banking Management System API")
                .description("MongoDB embedded document demo — Account + Transactions")
                .version("1.0.0"));
    }
}
```

### DataSeeder.java

File: `src/main/java/com/banking/bankingmongodemo/config/DataSeeder.java`

```java
package com.banking.bankingmongodemo.config;

import com.banking.bankingmongodemo.dto.CreateAccountRequest;
import com.banking.bankingmongodemo.dto.TransactionRequest;
import com.banking.bankingmongodemo.model.TransactionType;
import com.banking.bankingmongodemo.service.BankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    @Bean
    ApplicationRunner seedData(BankingService svc) {
        return args -> {
            // Account 1
            var req1 = new CreateAccountRequest();
            req1.setOwnerName("Alice Johnson");
            req1.setEmail("alice@bank.com");
            req1.setAccountType("SAVINGS");
            req1.setInitialDeposit(new BigDecimal("5000.00"));
            var alice = svc.createAccount(req1);

            // Account 2
            var req2 = new CreateAccountRequest();
            req2.setOwnerName("Bob Smith");
            req2.setEmail("bob@bank.com");
            req2.setAccountType("CURRENT");
            req2.setInitialDeposit(new BigDecimal("10000.00"));
            var bob = svc.createAccount(req2);

            // Deposit
            var deposit = new TransactionRequest();
            deposit.setType(TransactionType.DEPOSIT);
            deposit.setAmount(new BigDecimal("2000.00"));
            deposit.setDescription("Salary");
            svc.deposit(alice.getAccountNumber(), deposit);

            // Withdraw
            var withdraw = new TransactionRequest();
            withdraw.setType(TransactionType.WITHDRAWAL);
            withdraw.setAmount(new BigDecimal("500.00"));
            withdraw.setDescription("Rent");
            svc.withdraw(alice.getAccountNumber(), withdraw);

            // Transfer
            var transfer = new TransactionRequest();
            transfer.setType(TransactionType.TRANSFER);
            transfer.setAmount(new BigDecimal("1000.00"));
            transfer.setDescription("Payment to Bob");
            transfer.setTargetAccountNumber(bob.getAccountNumber());
            svc.transfer(alice.getAccountNumber(), transfer);

            System.out.println("✅ Banking seed data loaded");
            System.out.println("   Alice account: " + alice.getAccountNumber());
            System.out.println("   Bob account:   " + bob.getAccountNumber());
        };
    }
}
```

---

## Step 10 — Final Project Structure

```
banking-mongo-demo/
├── src/main/java/com/banking/bankingmongodemo/
│   ├── BankingMongoDemoApplication.java
│   ├── config/
│   │   ├── SwaggerConfig.java
│   │   └── DataSeeder.java
│   ├── controller/
│   │   └── BankingController.java
│   ├── dto/
│   │   ├── CreateAccountRequest.java
│   │   └── TransactionRequest.java
│   ├── model/
│   │   ├── Account.java
│   │   ├── Transaction.java
│   │   └── TransactionType.java
│   ├── repository/
│   │   └── AccountRepository.java
│   └── service/
│       └── BankingService.java
└── src/main/resources/
    └── application.properties
```

---

## Step 11 — Run and Test in Swagger

1. Right-click `BankingMongoDemoApplication.java` → **Run As → Spring Boot App**
2. Watch the console — you'll see:
   ```
   ✅ Banking seed data loaded
      Alice account: ACC-XXXXXX
      Bob account:   ACC-YYYYYY
   ```
3. Open browser → `http://localhost:8080/swagger-ui.html`

### Swagger Testing Flow

**① GET /api/banking/accounts**
- See Alice and Bob with their embedded transactions already present

**② POST /api/banking/accounts** — create a new account:
```json
{
  "ownerName": "Carol White",
  "email": "carol@bank.com",
  "accountType": "SAVINGS",
  "initialDeposit": 3000.00
}
```

**③ POST /api/banking/accounts/{accountNumber}/deposit**:
```json
{
  "type": "DEPOSIT",
  "amount": 500.00,
  "description": "Freelance payment"
}
```

**④ POST /api/banking/accounts/{accountNumber}/withdraw**:
```json
{
  "type": "WITHDRAWAL",
  "amount": 200.00,
  "description": "Groceries"
}
```

**⑤ POST /api/banking/accounts/{accountNumber}/transfer**:
```json
{
  "type": "TRANSFER",
  "amount": 300.00,
  "description": "Rent split",
  "targetAccountNumber": "ACC-YYYYYY"
}
```

**⑥ GET /api/banking/accounts/{accountNumber}/transactions**
- See all transactions embedded inside the account document

**⑦ Verify in MongoDB directly**:
```bash
mongosh
use bankingdb
db.accounts.find().pretty()
```
Every transaction is embedded inside its Account document — no separate collection, no joins.

---

## Key MongoDB Concepts Demonstrated

| Concept                    | Where It Appears                                                |
|----------------------------|-----------------------------------------------------------------|
| `@Document`                | `Account` — root collection                                     |
| Embedded subdocument       | `Transaction` inside `Account.transactions[]`                   |
| `@Indexed(unique = true)`  | `accountNumber` and `email` fields                              |
| `@Version`                 | Optimistic locking on `Account`                                 |
| Dot-notation `@Query`      | `findByTransactionId` — searching inside embedded array         |
| Single save = full graph   | `accountRepo.save(account)` persists account + all transactions |
| No JOIN needed             | Balance, history, and metadata all in one document fetch        |

---

*Guide prepared for: Banking Management System — Spring Boot + Spring Data MongoDB + Swagger UI*
