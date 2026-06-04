package com.wipro.BankManagementSystem.config;

import com.wipro.BankManagementSystem.entity.*;
import com.wipro.BankManagementSystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

/**
 * DataSeeder — runs automatically on every app startup.
 * Populates: Branch → Customer → Account → Transaction → User (admin + users)
 * Skips seeding if data already exists (safe to restart app multiple times).
 */
@Configuration
public class DataSeeder implements CommandLineRunner {

    @Autowired private BranchRepository     branchRepository;
    @Autowired private CustomerRepository   customerRepository;
    @Autowired private AccountRepository    accountRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private UserRepository       userRepository;
    @Autowired private PasswordEncoder      passwordEncoder;

    @Override
    public void run(String... args) {

        // ── Guard: skip if already seeded ──────────────────────────────────
        if (branchRepository.count() > 0) {
            System.out.println("✅ [DataSeeder] Data already exists — skipping seed.");
            return;
        }

        System.out.println("🌱 [DataSeeder] Seeding BankVerse data...");

        // ══════════════════════════════════════════════════════════════════
        // 1. BRANCHES
        // ══════════════════════════════════════════════════════════════════
        Branch hyd  = branch("BankVerse Hyderabad Main",  "Hyderabad, Telangana");
        Branch mum  = branch("BankVerse Mumbai Central",  "Mumbai, Maharashtra");
        Branch blr  = branch("BankVerse Bangalore Tech",  "Bangalore, Karnataka");
        Branch del  = branch("BankVerse Delhi Capital",   "New Delhi, Delhi");
        Branch che  = branch("BankVerse Chennai South",   "Chennai, Tamil Nadu");

        branchRepository.saveAll(List.of(hyd, mum, blr, del, che));

        // ══════════════════════════════════════════════════════════════════
        // 2. CUSTOMERS
        // ══════════════════════════════════════════════════════════════════
        // Hyderabad branch customers
        Customer c1 = customer("Arjun Reddy",       "arjun.reddy@gmail.com",     "9876543210", hyd);
        Customer c2 = customer("Priya Sharma",       "priya.sharma@gmail.com",    "9812345678", hyd);
        Customer c3 = customer("Rohit Verma",        "rohit.verma@gmail.com",     "9823456789", hyd);

        // Mumbai branch customers
        Customer c4 = customer("Sneha Patil",        "sneha.patil@gmail.com",     "9834567890", mum);
        Customer c5 = customer("Kiran Mehta",        "kiran.mehta@gmail.com",     "9845678901", mum);

        // Bangalore branch customers
        Customer c6 = customer("Ananya Nair",        "ananya.nair@gmail.com",     "9856789012", blr);
        Customer c7 = customer("Vikram Singh",       "vikram.singh@gmail.com",    "9867890123", blr);

        // Delhi branch customers
        Customer c8 = customer("Neha Gupta",         "neha.gupta@gmail.com",      "9878901234", del);
        Customer c9 = customer("Aditya Kumar",       "aditya.kumar@gmail.com",    "9889012345", del);

        // Chennai branch customers
        Customer c10 = customer("Divya Krishnan",   "divya.krishnan@gmail.com",  "9890123456", che);

        customerRepository.saveAll(List.of(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10));

        // ══════════════════════════════════════════════════════════════════
        // 3. ACCOUNTS
        // ══════════════════════════════════════════════════════════════════
        Account a1  = account("SAVINGS",  85000.00,  c1);
        Account a2  = account("CURRENT",  250000.00, c1);   // c1 has 2 accounts
        Account a3  = account("SAVINGS",  42000.00,  c2);
        Account a4  = account("SAVINGS",  130000.00, c3);
        Account a5  = account("CURRENT",  500000.00, c4);
        Account a6  = account("SAVINGS",  67000.00,  c5);
        Account a7  = account("SAVINGS",  91000.00,  c6);
        Account a8  = account("CURRENT",  320000.00, c7);
        Account a9  = account("SAVINGS",  55000.00,  c8);
        Account a10 = account("SAVINGS",  78000.00,  c9);
        Account a11 = account("SAVINGS",  110000.00, c10);
        Account a12 = account("CURRENT",  45000.00,  c3);   // c3 has 2 accounts

        accountRepository.saveAll(List.of(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12));

        // ══════════════════════════════════════════════════════════════════
        // 4. TRANSACTIONS
        // ══════════════════════════════════════════════════════════════════
        // Account a1 — Arjun's savings
        transactionRepository.saveAll(List.of(
            txn("DEPOSIT",  50000.00, LocalDate.of(2025, 1, 5),  a1),
            txn("DEPOSIT",  20000.00, LocalDate.of(2025, 2, 10), a1),
            txn("WITHDRAW", 10000.00, LocalDate.of(2025, 2, 15), a1),
            txn("DEPOSIT",  30000.00, LocalDate.of(2025, 3, 1),  a1),
            txn("WITHDRAW",  5000.00, LocalDate.of(2025, 3, 20), a1)
        ));

        // Account a2 — Arjun's current
        transactionRepository.saveAll(List.of(
            txn("DEPOSIT",  200000.00, LocalDate.of(2025, 1, 10), a2),
            txn("WITHDRAW",  50000.00, LocalDate.of(2025, 2, 5),  a2),
            txn("DEPOSIT",  100000.00, LocalDate.of(2025, 3, 15), a2)
        ));

        // Account a3 — Priya's savings
        transactionRepository.saveAll(List.of(
            txn("DEPOSIT",  40000.00, LocalDate.of(2025, 1, 8),  a3),
            txn("WITHDRAW",  8000.00, LocalDate.of(2025, 2, 20), a3),
            txn("DEPOSIT",  10000.00, LocalDate.of(2025, 3, 5),  a3)
        ));

        // Account a4 — Rohit's savings
        transactionRepository.saveAll(List.of(
            txn("DEPOSIT",  100000.00, LocalDate.of(2025, 1, 15), a4),
            txn("WITHDRAW",  20000.00, LocalDate.of(2025, 2, 12), a4),
            txn("DEPOSIT",   50000.00, LocalDate.of(2025, 3, 22), a4)
        ));

        // Account a5 — Sneha's current
        transactionRepository.saveAll(List.of(
            txn("DEPOSIT",  500000.00, LocalDate.of(2025, 1, 3),  a5),
            txn("WITHDRAW", 100000.00, LocalDate.of(2025, 2, 18), a5),
            txn("TRANSFER", 50000.00,  LocalDate.of(2025, 3, 10), a5),
            txn("DEPOSIT",  150000.00, LocalDate.of(2025, 4, 1),  a5)
        ));

        // Account a6 — Kiran's savings
        transactionRepository.saveAll(List.of(
            txn("DEPOSIT",  60000.00, LocalDate.of(2025, 1, 20), a6),
            txn("WITHDRAW", 15000.00, LocalDate.of(2025, 3, 8),  a6),
            txn("DEPOSIT",  22000.00, LocalDate.of(2025, 4, 5),  a6)
        ));

        // Account a7 — Ananya's savings
        transactionRepository.saveAll(List.of(
            txn("DEPOSIT",  80000.00, LocalDate.of(2025, 2, 1),  a7),
            txn("WITHDRAW", 10000.00, LocalDate.of(2025, 2, 25), a7),
            txn("DEPOSIT",  21000.00, LocalDate.of(2025, 4, 10), a7)
        ));

        // Account a8 — Vikram's current
        transactionRepository.saveAll(List.of(
            txn("DEPOSIT",  300000.00, LocalDate.of(2025, 1, 25), a8),
            txn("TRANSFER",  50000.00, LocalDate.of(2025, 2, 28), a8),
            txn("WITHDRAW",  30000.00, LocalDate.of(2025, 3, 30), a8)
        ));

        // Account a9 — Neha's savings
        transactionRepository.saveAll(List.of(
            txn("DEPOSIT",  50000.00, LocalDate.of(2025, 2, 5),  a9),
            txn("WITHDRAW",  5000.00, LocalDate.of(2025, 3, 15), a9),
            txn("DEPOSIT",  10000.00, LocalDate.of(2025, 4, 20), a9)
        ));

        // Account a10 — Aditya's savings
        transactionRepository.saveAll(List.of(
            txn("DEPOSIT",  70000.00, LocalDate.of(2025, 1, 18), a10),
            txn("WITHDRAW", 12000.00, LocalDate.of(2025, 2, 22), a10),
            txn("DEPOSIT",  20000.00, LocalDate.of(2025, 4, 15), a10)
        ));

        // Account a11 — Divya's savings
        transactionRepository.saveAll(List.of(
            txn("DEPOSIT",  100000.00, LocalDate.of(2025, 1, 12), a11),
            txn("WITHDRAW",  10000.00, LocalDate.of(2025, 3, 3),  a11),
            txn("DEPOSIT",   20000.00, LocalDate.of(2025, 4, 25), a11)
        ));

        // Account a12 — Rohit's current
        transactionRepository.saveAll(List.of(
            txn("DEPOSIT",  45000.00, LocalDate.of(2025, 2, 14), a12),
            txn("WITHDRAW", 10000.00, LocalDate.of(2025, 3, 18), a12)
        ));

        // ══════════════════════════════════════════════════════════════════
        // 5. USERS  (for JWT / OAuth2 login)
        // ══════════════════════════════════════════════════════════════════
        User admin = new User();
        admin.setEmail("admin@bankverse.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole("ROLE_ADMIN");
        admin.setProvider("local");
        admin.setName("BankVerse Admin");

        User user1 = new User();
        user1.setEmail("arjun@bankverse.com");
        user1.setPassword(passwordEncoder.encode("user123"));
        user1.setRole("ROLE_USER");
        user1.setProvider("local");
        user1.setName("Arjun Reddy");

        User user2 = new User();
        user2.setEmail("priya@bankverse.com");
        user2.setPassword(passwordEncoder.encode("user123"));
        user2.setRole("ROLE_USER");
        user2.setProvider("local");
        user2.setName("Priya Sharma");

        userRepository.saveAll(List.of(admin, user1, user2));

        System.out.println("✅ [DataSeeder] Seeding complete!");
        System.out.println("   → 5 Branches | 10 Customers | 12 Accounts | 34 Transactions");
        System.out.println("   → 3 Users seeded:");
        System.out.println("      ADMIN : admin@bankverse.com  / admin123");
        System.out.println("      USER  : arjun@bankverse.com  / user123");
        System.out.println("      USER  : priya@bankverse.com  / user123");
    }

    // ── Helper methods ────────────────────────────────────────────────────

    private Branch branch(String name, String location) {
        Branch b = new Branch();
        b.setBranchName(name);
        b.setBranchLocation(location);
        return b;
    }

    private Customer customer(String name, String email, String mobile, Branch branch) {
        Customer c = new Customer();
        c.setCustomerName(name);
        c.setEmail(email);
        c.setMobile(mobile);
        c.setBranch(branch);
        return c;
    }

    private Account account(String type, double balance, Customer customer) {
        Account a = new Account();
        a.setAccountType(type);
        a.setBalance(balance);
        a.setCustomer(customer);
        return a;
    }

    private Transaction txn(String type, double amount, LocalDate date, Account account) {
        Transaction t = new Transaction();
        t.setTransactionType(type);
        t.setAmount(amount);
        t.setTransactionDate(date);
        t.setAccount(account);
        return t;
    }
}
