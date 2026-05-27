package com.wipro.BankManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wipro.BankManagementSystem.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}