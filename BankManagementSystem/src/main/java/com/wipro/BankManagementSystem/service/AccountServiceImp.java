package com.wipro.BankManagementSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.BankManagementSystem.entity.Account;
import com.wipro.BankManagementSystem.repository.AccountRepository;

@Service
public class AccountServiceImp implements IAccountService {

    @Autowired
    private AccountRepository repo;

    @Override
    public Account saveAccount(Account account) {
        return repo.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return repo.findAll();
    }

    @Override
    public Account getAccountById(Long id) {
        return repo.findById(id).orElse(null);
    }

    //@Override
    //public void deleteAccount(Long id) {
      //  repo.deleteById(id);
    //}

    @Override
    public Account updateAccount(Long id, Account account) {
        Account existing = repo.findById(id).orElse(null);

        if (existing != null) {
            existing.setAccountNo(account.getAccountNo());
            existing.setAccountType(account.getAccountType());
            existing.setBalance(account.getBalance());
            return repo.save(existing);
        }
        return null;
    }
}
