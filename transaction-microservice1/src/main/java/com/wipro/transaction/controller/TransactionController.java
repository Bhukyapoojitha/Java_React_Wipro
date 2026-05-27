package com.wipro.transaction.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wipro.transaction.entity.Transaction;
import com.wipro.transaction.service.TransactionService;
import com.wipro.transaction.dto.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService service;

    // ✅ Deposit
    @PostMapping("/deposit")
    public String deposit(@RequestBody TransactionRequest request) {
        return service.deposit(request);
    }

    // ✅ Withdraw
    @PostMapping("/withdraw")
    public String withdraw(@RequestBody TransactionRequest request) {
        return service.withdraw(request);
    }

    // ✅ Transfer
    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest request) {
        return service.transfer(request);
    }

    // ✅ Get transactions by accountNo
    @GetMapping("/account/{accountNo}")   // ✅ FIXED name
    public List<Transaction> getTransactions(@PathVariable String accountNo) {
        return service.getTransactionsByAccount(accountNo);
    }
}
