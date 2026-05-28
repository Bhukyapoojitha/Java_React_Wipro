package com.wipro.bank.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wipro.bank.entity.Transaction;
import com.wipro.bank.entity.TransactionType;

public interface TransactionRepository 
         extends JpaRepository<Transaction, Long> {

    // ✅ Full transaction history
    List<Transaction> 
    findBySenderAccountAccountIdOrReceiverAccountAccountId(
            Long senderAccountId,
            Long receiverAccountId
    );

    // ✅ Mini statement
    List<Transaction> 
    findTop5BySenderAccountAccountIdOrReceiverAccountAccountIdOrderByTransactionDateDesc(
            Long senderAccount,
            Long receiverAccount
    );

    // ✅ Filter by transaction type
    List<Transaction> 
    findByTransactionType(TransactionType transactionType);
}