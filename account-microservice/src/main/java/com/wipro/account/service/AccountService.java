package com.wipro.account.service;

import com.wipro.account.entity.Account;

public interface AccountService {

    Account createAccount(Account account);

    Account getAccountById(String accountNo);

    Double getBalance(String accountNo);

    Account updateBalance(String accountNo, Double balance);
}