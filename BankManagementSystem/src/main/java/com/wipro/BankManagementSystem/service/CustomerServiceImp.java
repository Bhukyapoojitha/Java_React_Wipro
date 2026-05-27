package com.wipro.BankManagementSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.BankManagementSystem.entity.Customer;
import com.wipro.BankManagementSystem.repository.CustomerRepository;

@Service
public class CustomerServiceImp implements ICustomerService {

    @Autowired
    private CustomerRepository repo;

    // ✅ Save customer
    public Customer saveCustomer(Customer customer) {
        return repo.save(customer);
    }

    // ✅ Get all customers
    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }

    // ✅ Get customer by ID
    public Customer getCustomerById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // ✅ Delete customer
    //public void deleteCustomer(Long id) {
    //    repo.deleteById(id);
   // }

    // ✅ Update customer
    public Customer updateCustomer(Long id, Customer customer) {
        Customer existing = repo.findById(id).orElse(null);

        if (existing != null) {
            existing.setName(customer.getName());
            existing.setEmail(customer.getEmail());
            return repo.save(existing);
        }
        return null;
    }
}