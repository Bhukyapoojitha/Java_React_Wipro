
package com.wipro.transaction.repository;

import com.wipro.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByStatus(String status);

    List<Transaction> findByType(String type);

    List<Transaction> findBySenderAccount(String sender);

    List<Transaction> findByReceiverAccount(String receiver);

    List<Transaction> findByAmountGreaterThan(double amount);
}
