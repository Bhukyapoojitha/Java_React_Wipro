package com.wipro.bank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.bank.entity.Account;
import com.wipro.bank.entity.AccountType;
import com.wipro.bank.entity.Customer;

@Repository
public interface AccountRepository 
extends JpaRepository<Account, Long> {
	
	Optional<Account> findByAccountNumber(String accountNumber);
	
	List<Account> findByCustomerCustomerId(Long customerId);
	
	List<Account> findByAccountType(AccountType accountType);
	
	List<Account> findByBranchBranchId(Long branchId);
	

}
