package com.wipro.transaction.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.wipro.transaction.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountNo(String accountNo);

    List<Transaction> findByAccountNoOrderByTxnDateDesc(String accountNo);

    List<Transaction> findByStatus(String status);
}
