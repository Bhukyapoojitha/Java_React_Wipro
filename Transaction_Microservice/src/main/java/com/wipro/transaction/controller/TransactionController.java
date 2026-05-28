package com.wipro.transaction.controller;

import com.wipro.transaction.entity.Transaction;
import com.wipro.transaction.service.TransactionService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    // ✅ Create Transaction
    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return service.createTransaction(transaction);
    }

    // ✅ Get All Transactions
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return service.getAllTransactions();
    }

    // ✅ Get by ID
    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return service.getTransactionById(id);
    }

    // ✅ Update Status
    @PutMapping("/{id}")
    public Transaction updateStatus(@PathVariable Long id,
                                    @RequestParam String status) {
        return service.updateStatus(id, status);
    }

    // ✅ Filter by Status
    @GetMapping("/status/{status}")
    public List<Transaction> getByStatus(@PathVariable String status) {
        return service.getByStatus(status);
    }

    // ✅ Filter by Type
    @GetMapping("/type/{type}")
    public List<Transaction> getByType(@PathVariable String type) {
        return service.getByType(type);
    }
}