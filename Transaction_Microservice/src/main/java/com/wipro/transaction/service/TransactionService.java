package com.wipro.transaction.service;

import com.wipro.transaction.entity.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(Transaction transaction);

    List<Transaction> getAllTransactions();

    Transaction getTransactionById(Long id);

    Transaction updateStatus(Long id, String status);

    //void deleteTransaction(Long id);

    List<Transaction> getByStatus(String status);

    List<Transaction> getByType(String type);
}
