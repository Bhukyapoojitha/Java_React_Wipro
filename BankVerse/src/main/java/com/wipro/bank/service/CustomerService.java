package com.wipro.bank.service;

import com.wipro.bank.dto.CustomerRequestDTO;
import com.wipro.bank.dto.CustomerResponseDTO;

import java.util.List;

public interface CustomerService {

    CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO);

    List<CustomerResponseDTO> getAllCustomers();

    CustomerResponseDTO getCustomerById(Long customerId);

    CustomerResponseDTO updateCustomer(Long customerId,
                                       CustomerRequestDTO requestDTO);
}