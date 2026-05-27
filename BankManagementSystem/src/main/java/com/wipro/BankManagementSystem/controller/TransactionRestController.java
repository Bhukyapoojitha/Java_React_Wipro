package com.wipro.BankManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wipro.BankManagementSystem.entity.Transaction;
import com.wipro.BankManagementSystem.service.ITransactionService;

@RestController                      // ✅ VERY IMPORTANT
@RequestMapping("/transactions")     // ✅ Base URL
public class TransactionRestController {

    @Autowired
    private ITransactionService service;

    @PostMapping
    public Transaction addTransaction(@RequestBody Transaction transaction) {
        return service.saveTransaction(transaction);
    }


    @GetMapping
    public List<Transaction> getAllTransactions() {
        return service.getAllTransactions();
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return service.getTransactionById(id);
    }

    
}

