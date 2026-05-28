package com.wipro.bank.service;

import com.wipro.bank.dto.TransactionRequestDTO;
import com.wipro.bank.dto.TransactionResponseDTO;
import com.wipro.bank.entity.*;
import com.wipro.bank.exception.InsufficientBalanceException;
import com.wipro.bank.exception.ResourceNotFoundException;
import com.wipro.bank.repository.AccountRepository;
import com.wipro.bank.repository.TransactionRepository;
import com.wipro.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl
        implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    public TransactionResponseDTO deposit(
            TransactionRequestDTO requestDTO) {

        Account account =
                accountRepository.findById(
                                requestDTO.getReceiverAccountId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account not found"));

        account.setBalance(
                account.getBalance()
                        .add(requestDTO.getAmount()));

        accountRepository.save(account);

        Transaction transaction =
                Transaction.builder()
                        .transactionType(
                                TransactionType.DEPOSIT)
                        .amount(requestDTO.getAmount())
                        .remarks(requestDTO.getRemarks())
                        .transactionDate(LocalDateTime.now())
                        .receiverAccount(account)
                        .build();

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        return mapToResponse(savedTransaction, account);
    }

    @Override
    public TransactionResponseDTO withdraw(
            TransactionRequestDTO requestDTO) {

        Account account =
                accountRepository.findById(
                                requestDTO.getSenderAccountId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account not found"));

        if(account.getBalance()
                .compareTo(requestDTO.getAmount()) < 0){
            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }

        account.setBalance(
                account.getBalance()
                        .subtract(requestDTO.getAmount()));

        accountRepository.save(account);

        Transaction transaction =
                Transaction.builder()
                        .transactionType(
                                TransactionType.WITHDRAW)
                        .amount(requestDTO.getAmount())
                        .remarks(requestDTO.getRemarks())
                        .transactionDate(LocalDateTime.now())
                        .senderAccount(account)
                        .build();

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        return mapToResponse(savedTransaction, account);
    }

    @Override
    public TransactionResponseDTO transfer(
            TransactionRequestDTO requestDTO) {

        Account sender =
                accountRepository.findById(
                                requestDTO.getSenderAccountId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Sender account not found"));

        Account receiver =
                accountRepository.findById(
                                requestDTO.getReceiverAccountId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Receiver account not found"));

        if(sender.getBalance()
                .compareTo(requestDTO.getAmount()) < 0){
            throw new InsufficientBalanceException(
                    "Insufficient balance");
        }

        sender.setBalance(
                sender.getBalance()
                        .subtract(requestDTO.getAmount()));

        receiver.setBalance(
                receiver.getBalance()
                        .add(requestDTO.getAmount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction =
                Transaction.builder()
                        .transactionType(
                                TransactionType.TRANSFER)
                        .amount(requestDTO.getAmount())
                        .remarks(requestDTO.getRemarks())
                        .transactionDate(LocalDateTime.now())
                        .senderAccount(sender)
                        .receiverAccount(receiver)
                        .build();

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        return TransactionResponseDTO.builder()
                .transactionId(
                        savedTransaction.getTransactionId())
                .transactionType(
                        savedTransaction.getTransactionType())
                .fromAccount(
                        sender.getAccountNumber())
                .toAccount(
                        receiver.getAccountNumber())
                .amount(savedTransaction.getAmount())
                .senderBalance(sender.getBalance())
                .receiverBalance(receiver.getBalance())
                .status("SUCCESS")
                .message("Transfer successful")
                .remarks(savedTransaction.getRemarks())
                .transactionDate(
                        savedTransaction.getTransactionDate())
                .build();
    }

    @Override
    public List<TransactionResponseDTO>
    getTransactionsByAccount(Long accountId) {

        return transactionRepository
                .findBySenderAccountAccountIdOrReceiverAccountAccountId(
                        accountId,
                        accountId)
                .stream()
                .map(this::mapTransactionResponse)
                .toList();
    }

    @Override
    public List<TransactionResponseDTO>
    getMiniStatement(Long accountId) {

        return transactionRepository
                .findTop5BySenderAccountAccountIdOrReceiverAccountAccountIdOrderByTransactionDateDesc(
                        accountId,
                        accountId)
                .stream()
                .map(this::mapTransactionResponse)
                .toList();
    }

    @Override
    public List<TransactionResponseDTO>
    getTransactionsByType(
            TransactionType transactionType) {

        return transactionRepository
                .findByTransactionType(transactionType)
                .stream()
                .map(this::mapTransactionResponse)
                .toList();
    }

    private TransactionResponseDTO mapToResponse(
            Transaction transaction,
            Account account) {

        return TransactionResponseDTO.builder()
                .transactionId(transaction.getTransactionId())
                .transactionType(transaction.getTransactionType())
                .toAccount(account.getAccountNumber())
                .amount(transaction.getAmount())
                .updatedBalance(account.getBalance())
                .status("SUCCESS")
                .message("Transaction successful")
                .remarks(transaction.getRemarks())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }

    private TransactionResponseDTO mapTransactionResponse(
            Transaction transaction) {

        return TransactionResponseDTO.builder()
                .transactionId(transaction.getTransactionId())
                .transactionType(transaction.getTransactionType())
                .fromAccount(
                        transaction.getSenderAccount() != null
                                ? transaction.getSenderAccount().getAccountNumber()
                                : null)
                .toAccount(
                        transaction.getReceiverAccount() != null
                                ? transaction.getReceiverAccount().getAccountNumber()
                                : null)
                .amount(transaction.getAmount())
                .remarks(transaction.getRemarks())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }
}