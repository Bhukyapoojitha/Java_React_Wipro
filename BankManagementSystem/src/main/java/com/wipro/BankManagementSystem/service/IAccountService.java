package com.wipro.BankManagementSystem.service;

import java.util.List;

import com.wipro.BankManagementSystem.entity.Account;

public interface IAccountService {

    // ✅ Create
    Account saveAccount(Account account);

    // ✅ Read
    List<Account> getAllAccounts();

    Account getAccountById(Long id);

    // ✅ Update
    Account updateAccount(Long id, Account account);

    // ✅ Delete
   // void deleteAccount(Long id);
}

