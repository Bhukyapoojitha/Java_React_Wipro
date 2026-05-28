package com.wipro.account.service;

import com.wipro.account.entity.Account;

import java.util.List;

public interface AccountService {

    Account createAccount(Account account);

    List<Account> getAllAccounts();

    Account getAccount(String accountNumber);

    Account updateAccount(
            String accountNumber,
            Account updatedAccount
    );

    //void deleteAccount(String accountNumber);

    void debit(String accountNumber, double amount);

    void credit(String accountNumber, double amount);
}
