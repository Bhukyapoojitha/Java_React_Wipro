package com.wipro.mongodemo.repository;

import com.wipro.mongodemo.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String> {

    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByEmail(String email);

    List<Account> findByAccountType(String accountType);

    // Query into embedded transaction array using dot notation
    @Query("{ 'transactions.transactionId': ?0 }")
    Optional<Account> findByTransactionId(String transactionId);

    @Query("{ 'transactions.type': ?0 }")
    List<Account> findAccountsWithTransactionType(String type);
}
