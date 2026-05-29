package com.wipro.mongodemo.config;

import com.wipro.mongodemo.dto.CreateAccountRequest;
import com.wipro.mongodemo.dto.TransactionRequest;
import com.wipro.mongodemo.model.TransactionType;
import com.wipro.mongodemo.service.BankingService;
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
