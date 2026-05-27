package com.wipro.BankManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wipro.BankManagementSystem.entity.Account;
import com.wipro.BankManagementSystem.service.IAccountService;

@RestController                   // ✅ VERY IMPORTANT
@RequestMapping("/accounts")      // ✅ Base URL
public class AccountRestController {

    @Autowired
    private IAccountService service;

    @PostMapping
    public Account addAccount(@RequestBody Account account) {
        return service.saveAccount(account);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return service.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return service.getAccountById(id);
    }

    // ✅ Update account
    @PutMapping("/{id}")
    public Account updateAccount(@PathVariable Long id,
                                 @RequestBody Account account) {
        return service.updateAccount(id, account);
    }

}
