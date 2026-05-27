package com.wipro.transaction.service;

import java.util.List;
import com.wipro.transaction.entity.Transaction;
import com.wipro.transaction.dto.TransactionRequest;
import com.wipro.transaction.dto.TransferRequest;

public interface TransactionService {

    String deposit(TransactionRequest request);

    String withdraw(TransactionRequest request);

    String transfer(TransferRequest request);

    List<Transaction> getTransactionsByAccount(String accountId);
}