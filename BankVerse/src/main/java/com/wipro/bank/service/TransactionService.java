package com.wipro.bank.service;

import com.wipro.bank.dto.TransactionRequestDTO;
import com.wipro.bank.dto.TransactionResponseDTO;
import com.wipro.bank.entity.TransactionType;

import java.util.List;

public interface TransactionService {

    TransactionResponseDTO deposit(TransactionRequestDTO requestDTO);

    TransactionResponseDTO withdraw(TransactionRequestDTO requestDTO);

    TransactionResponseDTO transfer(TransactionRequestDTO requestDTO);

    List<TransactionResponseDTO> getTransactionsByAccount(Long accountId);

    List<TransactionResponseDTO> getMiniStatement(Long accountId);

    List<TransactionResponseDTO> getTransactionsByType(TransactionType transactionType);
}
