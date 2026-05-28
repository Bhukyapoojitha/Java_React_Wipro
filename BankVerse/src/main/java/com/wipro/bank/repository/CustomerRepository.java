package com.wipro.bank.repository;
import com.wipro.bank.entity.Customer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository 
         extends JpaRepository<Customer, Long>  {
	
	Optional<Customer> findByEmail(String email);
	
	List<Customer> findByBranch_BranchId(Long branchId);
	
}
