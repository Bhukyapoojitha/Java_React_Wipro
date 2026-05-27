package com.wipro.BankManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.BankManagementSystem.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // optional custom method
    Customer findByEmail(String email);
}
