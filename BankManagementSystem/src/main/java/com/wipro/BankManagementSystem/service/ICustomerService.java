package com.wipro.BankManagementSystem.service;

import java.util.List;

import com.wipro.BankManagementSystem.entity.Customer;

public interface ICustomerService {

    // ✅ Create
    Customer saveCustomer(Customer customer);

    // ✅ Read
    List<Customer> getAllCustomers();

    Customer getCustomerById(Long id);

    // ✅ Update
    Customer updateCustomer(Long id, Customer customer);

    // ✅ Delete
    //void deleteCustomer(Long id);
}
