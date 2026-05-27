package com.wipro.transaction.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wipro.transaction.entity.Transaction;
import com.wipro.transaction.repository.TransactionRepository;
import com.wipro.transaction.dto.Account;
import com.wipro.transaction.dto.TransactionRequest;
import com.wipro.transaction.dto.TransferRequest;

@Service
public class TransactionServiceImp implements TransactionService {

    @Autowired
    private TransactionRepository repo;

    @Autowired
    private RestTemplate restTemplate;

    private final String ACCOUNT_URL = "http://localhost:8084/accounts/";

    // ✅ ✅ DEPOSIT
    @Override
    public String deposit(TransactionRequest request) {

        String accountNo = request.getAccountNo();
        Double amount = request.getAmount();

        if (amount <= 0) {
            saveTxn(accountNo, amount, "DEPOSIT", "FAILED");
            return "Invalid amount ❌";
        }

        Account acc = restTemplate.getForObject(
                ACCOUNT_URL + accountNo, Account.class);

        if (acc == null) {
            return "Account not found ❌";
        }

        Double newBalance = acc.getBalance() + amount;

        // ✅ Update balance
        restTemplate.put(
                ACCOUNT_URL + accountNo + "/balance?balance=" + newBalance,
                null
        );

        saveTxn(accountNo, amount, "DEPOSIT", "SUCCESS");

        return "Deposit successful ✅ New Balance: " + newBalance;
    }

    // ✅ ✅ WITHDRAW
    @Override
    public String withdraw(TransactionRequest request) {

        String accountNo = request.getAccountNo();
        Double amount = request.getAmount();

        Account acc = restTemplate.getForObject(
                ACCOUNT_URL + accountNo, Account.class);

        if (acc == null) {
            return "Account not found ❌";
        }

        if (amount <= 0 || acc.getBalance() < amount) {
            saveTxn(accountNo, amount, "WITHDRAW", "FAILED");
            return "Invalid or insufficient balance ❌";
        }

        Double newBalance = acc.getBalance() - amount;

        // ✅ Update balance
        restTemplate.put(
                ACCOUNT_URL + accountNo + "/balance?balance=" + newBalance,
                null
        );

        saveTxn(accountNo, amount, "WITHDRAW", "SUCCESS");

        return "Withdraw successful ✅ New Balance: " + newBalance;
    }

    // ✅ ✅ TRANSFER
    @Override
    public String transfer(TransferRequest request) {

        String from = request.getFromAccount();
        String to = request.getToAccount();
        Double amount = request.getAmount();

        Account fromAcc = restTemplate.getForObject(
                ACCOUNT_URL + from, Account.class);

        Account toAcc = restTemplate.getForObject(
                ACCOUNT_URL + to, Account.class);

        if (fromAcc == null || toAcc == null) {
            return "One or both accounts not found ❌";
        }

        if (amount <= 0 || from.equals(to) || fromAcc.getBalance() < amount) {
            saveTxn(from, amount, "WITHDRAW", "FAILED");
            return "Invalid transfer ❌";
        }

        Double fromBalance = fromAcc.getBalance() - amount;
        Double toBalance = toAcc.getBalance() + amount;

        // ✅ Update balances
        restTemplate.put(
                ACCOUNT_URL + from + "/balance?balance=" + fromBalance,
                null
        );

        restTemplate.put(
                ACCOUNT_URL + to + "/balance?balance=" + toBalance,
                null
        );

        // ✅ Save transactions
        saveTxn(from, amount, "WITHDRAW", "SUCCESS");
        saveTxn(to, amount, "DEPOSIT", "SUCCESS");

        return "Transfer successful ✅ Sender Balance: " + fromBalance;
    }

    // ✅ ✅ GET TRANSACTIONS
    @Override
    public List<Transaction> getTransactionsByAccount(String accountNo) {
        return repo.findByAccountNoOrderByTxnDateDesc(accountNo);
    }

    // ✅ ✅ COMMON SAVE METHOD
    private void saveTxn(String accountNo, Double amount,
                         String type, String status) {

        Transaction txn = new Transaction();

        txn.setAccountNo(accountNo);
        txn.setAmount(amount);
        txn.setTxnType(type);
        txn.setTxnDate(new Date());
        txn.setStatus(status);

        repo.save(txn);
    }
}