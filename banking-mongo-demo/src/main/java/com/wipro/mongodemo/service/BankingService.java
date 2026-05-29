package com.wipro.mongodemo.service;

import com.wipro.mongodemo.dto.CreateAccountRequest;
import com.wipro.mongodemo.dto.TransactionRequest;
import com.wipro.mongodemo.model.Account;
import com.wipro.mongodemo.model.Transaction;
import com.wipro.mongodemo.model.TransactionType;
import com.wipro.mongodemo.repository.AccountRepository;
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
