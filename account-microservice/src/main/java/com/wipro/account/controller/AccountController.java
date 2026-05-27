package com.wipro.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wipro.account.entity.Account;
import com.wipro.account.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService service;

    // ✅ Create account
    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return service.createAccount(account);
    }

    // ✅ Get account
    @GetMapping("/{accountNo}")
    public Account getAccountById(@PathVariable String accountNo) {
        return service.getAccountById(accountNo);   // ✅ FIXED
    }

    // ✅ Get balance only
    @GetMapping("/{accountNo}/balance")
    public Double getBalance(@PathVariable String accountNo) {
        return service.getBalance(accountNo);
    }

    // ✅ Update balance
    @PutMapping("/{accountNo}/balance")
    public Account updateBalance(@PathVariable String accountNo,
                                @RequestParam Double balance) {
        return service.updateBalance(accountNo, balance);
    }
}