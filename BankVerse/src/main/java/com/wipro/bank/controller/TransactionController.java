package com.wipro.bank.controller;

import com.wipro.bank.dto.TransactionRequestDTO;
import com.wipro.bank.dto.TransactionResponseDTO;
import com.wipro.bank.entity.TransactionType;
import com.wipro.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(
            @RequestBody TransactionRequestDTO requestDTO) {
        return new ResponseEntity<>(
                transactionService.deposit(requestDTO),
                HttpStatus.OK
        );
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDTO> withdraw(
            @RequestBody TransactionRequestDTO requestDTO) {
        return new ResponseEntity<>(
                transactionService.withdraw(requestDTO),
                HttpStatus.OK
        );
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(
            @RequestBody TransactionRequestDTO requestDTO) {
        return new ResponseEntity<>(
                transactionService.transfer(requestDTO),
                HttpStatus.OK
        );
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<?> getTransactionsByAccount(
            @PathVariable Long accountId) {
        return ResponseEntity.ok(
                transactionService.getTransactionsByAccount(accountId)
        );
    }

    @GetMapping("/mini-statement/{accountId}")
    public ResponseEntity<?> getMiniStatement(
            @PathVariable Long accountId) {
        return ResponseEntity.ok(
                transactionService.getMiniStatement(accountId)
        );
    }

    @GetMapping("/type/{transactionType}")
    public ResponseEntity<?> getTransactionsByType(
            @PathVariable TransactionType transactionType) {
        return ResponseEntity.ok(
                transactionService.getTransactionsByType(transactionType)
        );
    }
}