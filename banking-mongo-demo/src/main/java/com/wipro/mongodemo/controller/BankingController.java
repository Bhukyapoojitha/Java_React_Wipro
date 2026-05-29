package com.wipro.mongodemo.controller;

import com.wipro.mongodemo.dto.CreateAccountRequest;
import com.wipro.mongodemo.dto.TransactionRequest;
import com.wipro.mongodemo.model.Account;
import com.wipro.mongodemo.model.Transaction;
import com.wipro.mongodemo.service.BankingService;
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
