package com.wipro.transaction.service;

import com.wipro.transaction.dto.AccountDTO;
import com.wipro.transaction.entity.Transaction;
import com.wipro.transaction.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final RestTemplate restTemplate;

    @Override
    public Transaction createTransaction(Transaction transaction) {

        // ✅ 1. Validate sender != receiver
        if (transaction.getSenderAccount()
                .equals(transaction.getReceiverAccount())) {
            throw new RuntimeException("Sender and Receiver cannot be same");
        }

        // ✅ 2. Fetch sender account
        AccountDTO sender = restTemplate.getForObject(
                "http://localhost:8081/accounts/" + transaction.getSenderAccount(),
                AccountDTO.class
        );

        // ✅ 3. Fetch receiver account
        AccountDTO receiver = restTemplate.getForObject(
                "http://localhost:8081/accounts/" + transaction.getReceiverAccount(),
                AccountDTO.class
        );

        if (sender == null || receiver == null) {
            throw new RuntimeException("Account not found");
        }

        // ✅ 4. Check balance
        if (sender.getBalance() < transaction.getAmount()) {
            throw new RuntimeException("Insufficient Balance");
        }

        // ✅ 5. Update balances (manual logic)
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        receiver.setBalance(receiver.getBalance() + transaction.getAmount());

        // ✅ 6. Call Account API (PUT)
        restTemplate.put(
                "http://localhost:8081/accounts/" + sender.getAccountNumber(),
                sender
        );

        restTemplate.put(
                "http://localhost:8081/accounts/" + receiver.getAccountNumber(),
                receiver
        );

        // ✅ 7. Set system fields
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType("TRANSFER");
        //transaction.setRetryCount(0);

//        // ✅ 8. Suspicious logic
//        if (transaction.getAmount() > 50000) {
//            transaction.setSuspicious(true);
//            transaction.setStatus("SUSPICIOUS");
//        } else {
//            transaction.setSuspicious(false);
//            transaction.setStatus("SUCCESS");
//        }

        // ✅ 9. Save transaction
        return repository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public Transaction updateStatus(Long id, String status) {
        Transaction transaction = getTransactionById(id);
        transaction.setStatus(status);
        return repository.save(transaction);
    }

    @Override
    public List<Transaction> getByStatus(String status) {
        return repository.findByStatus(status);
    }

    @Override
    public List<Transaction> getByType(String type) {
        return repository.findByType(type);
    }
}