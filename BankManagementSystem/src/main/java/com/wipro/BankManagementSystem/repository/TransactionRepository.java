package com.wipro.BankManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.BankManagementSystem.entity.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // ✅ Correct method (matches entity field)
    List<Transaction> findByTxnType(String txnType);

    List<Transaction> findByAmountGreaterThan(Double amount);
}