package com.wipro.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.account.entity.Account;
import com.wipro.account.repository.AccountRepository;

@Service
public class AccountServiceImp implements AccountService {

    @Autowired
    private AccountRepository repo;

    // ✅ Create
    @Override
    public Account createAccount(Account account) {
        return repo.save(account);
    }

    // ✅ FIXED → String accountNo (not Long)
    @Override
    public Account getAccountById(String accountNo) {
        return repo.findById(accountNo).orElse(null);
    }

    // ✅ FIXED
    @Override
    public Double getBalance(String accountNo) {
        Account acc = repo.findById(accountNo).orElse(null);
        return (acc != null) ? acc.getBalance() : null;
    }

    // ✅ FIXED
    @Override
    public Account updateBalance(String accountNo, Double balance) {

        Account acc = repo.findById(accountNo).orElse(null);

        if (acc != null) {
            acc.setBalance(balance);
            return repo.save(acc);
        }

        return null;
    }
}