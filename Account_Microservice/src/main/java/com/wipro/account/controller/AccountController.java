package com.wipro.account.controller;

import com.wipro.account.entity.Account;
import com.wipro.account.service.AccountService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @PostMapping
    public Account createAccount(
            @RequestBody Account account) {

        return service.createAccount(account);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return service.getAllAccounts();
    }

    @GetMapping("/{accountNumber}")
    public Account getAccount(
            @PathVariable String accountNumber) {

        return service.getAccount(accountNumber);
    }

    @PutMapping("/{accountNumber}")
    public Account updateAccount(
            @PathVariable String accountNumber,
            @RequestBody Account account) {

        return service.updateAccount(
                accountNumber,
                account
        );
    }

//    @DeleteMapping("/{accountNumber}")
//    public String deleteAccount(
//            @PathVariable String accountNumber) {
//
//        service.deleteAccount(accountNumber);
//
//        return "Account Deleted Successfully";
//    }

//    @PostMapping("/debit/{accountNumber}/{amount}")
//    public String debit(
//            @PathVariable String accountNumber,
//            @PathVariable double amount) {
//
//        service.debit(accountNumber, amount);
//
//        return "Amount Debited";
//    }
//
//    @PostMapping("/credit/{accountNumber}/{amount}")
//    public String credit(
//            @PathVariable String accountNumber,
//            @PathVariable double amount) {
//
//        service.credit(accountNumber, amount);
//
//        return "Amount Credited";
//    }
}
