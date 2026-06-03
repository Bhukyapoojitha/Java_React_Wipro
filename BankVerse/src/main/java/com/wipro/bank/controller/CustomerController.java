package com.wipro.bank.controller;

import com.wipro.bank.dto.CustomerRequestDTO;
import com.wipro.bank.dto.CustomerResponseDTO;
import com.wipro.bank.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(
            @RequestBody CustomerRequestDTO requestDTO) {
        return new ResponseEntity<>(
                customerService.createCustomer(requestDTO),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        return ResponseEntity.ok(
                customerService.getAllCustomers()
        );
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(
                customerService.getCustomerById(customerId)
        );
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable Long customerId,
            @RequestBody CustomerRequestDTO requestDTO) {
        return ResponseEntity.ok(
                customerService.updateCustomer(customerId, requestDTO)
        );
    }
}
