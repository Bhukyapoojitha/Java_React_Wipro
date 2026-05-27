package com.wipro.BankManagementSystem.service;

import java.util.List;

import com.wipro.BankManagementSystem.entity.Transaction;

public interface ITransactionService {

    // ✅ Create
    Transaction saveTransaction(Transaction transaction);

    // ✅ Read
    List<Transaction> getAllTransactions();

    Transaction getTransactionById(Long id);

    // ✅ Update
   // Transaction updateTransaction(Long id, Transaction transaction);

    // ✅ Delete
    //void deleteTransaction(Long id);
}