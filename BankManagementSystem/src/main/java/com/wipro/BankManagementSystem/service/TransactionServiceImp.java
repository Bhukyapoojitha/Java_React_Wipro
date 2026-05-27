package com.wipro.BankManagementSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.BankManagementSystem.entity.Transaction;
import com.wipro.BankManagementSystem.repository.TransactionRepository;

@Service
public class TransactionServiceImp implements ITransactionService {

    @Autowired
    private TransactionRepository repo;

    // ✅ Create / Save
    public Transaction saveTransaction(Transaction transaction) {
        return repo.save(transaction);
    }

    // ✅ Get all
    public List<Transaction> getAllTransactions() {
        return repo.findAll();
    }

    // ✅ Get by ID
    public Transaction getTransactionById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // ✅ Delete
   // public void deleteTransaction(Long id) {
    //    repo.deleteById(id);
   // }

   // public Transaction updateTransaction(Long id, Transaction transaction) {
     //   Transaction existing = repo.findById(id).orElse(null);

      //  if (existing != null) {
         //   existing.setAmount(transaction.getAmount());
         //   existing.setTxnType(transaction.getTxnType());  // ✅ FIXED
          //  return repo.save(existing);
      //  }
      //  return null;
   // }
    
}
