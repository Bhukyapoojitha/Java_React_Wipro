package com.wipro.account.service;

import com.wipro.account.entity.Account;
import com.wipro.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl
        implements AccountService {

    private final AccountRepository repository;

    @Override
    public Account createAccount(Account account) {

        account.setStatus("ACTIVE");

        return repository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    @Override
    public Account getAccount(String accountNumber) {

        return repository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Account Not Found"
                        ));
    }

    @Override
    public Account updateAccount(
            String accountNumber,
            Account updatedAccount) {

        Account account = getAccount(accountNumber);

        account.setAccountHolderName(
                updatedAccount.getAccountHolderName()
        );

        account.setAccountType(
                updatedAccount.getAccountType()
        );
        
        account.setBalance(updatedAccount.getBalance());

        return repository.save(account);
    }

//    @Override
//    public void deleteAccount(String accountNumber) {
//
//        Account account = getAccount(accountNumber);
//
//        repository.delete(account);
//    }

//    @Override
//    public void debit(String accountNumber,
//                      double amount) {
//
//        Account account = getAccount(accountNumber);
//
//        if(account.getBalance() < amount) {
//            throw new RuntimeException(
//                    "Insufficient Balance"
//            );
//        }
//
//        account.setBalance(
//                account.getBalance() - amount
//        );
//
//        repository.save(account);
//    }

//    @Override
//    public void credit(String accountNumber,
//                       double amount) {
//
//        Account account = getAccount(accountNumber);
//
//        account.setBalance(
//                account.getBalance() + amount
//        );

//        repository.save(account);
//    }
}


