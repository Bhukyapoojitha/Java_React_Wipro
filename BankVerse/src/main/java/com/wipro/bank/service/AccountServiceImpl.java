package com.wipro.bank.service;

import com.wipro.bank.dto.AccountRequestDTO;

import com.wipro.bank.dto.AccountResponseDTO;
import com.wipro.bank.entity.*;
import com.wipro.bank.exception.ResourceNotFoundException;
import com.wipro.bank.repository.AccountRepository;
import com.wipro.bank.repository.BranchRepository;
import com.wipro.bank.repository.CustomerRepository;
//import com.wipro.bank.service.AccountService;
import com.wipro.bank.entity.AccountType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final BranchRepository branchRepository;

    @Override
    public AccountResponseDTO createAccount(
            AccountRequestDTO requestDTO) {

        Customer customer =
                customerRepository.findById(
                                requestDTO.getCustomerId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found"));

        Branch branch =
                branchRepository.findById(
                                requestDTO.getBranchId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Branch not found"));

        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .accountType(requestDTO.getAccountType())
                .accountStatus(AccountStatus.ACTIVE)
                .balance(requestDTO.getBalance())
                .customer(customer)
                .branch(branch)
                .build();

        Account savedAccount =
                accountRepository.save(account);

        return mapToResponse(savedAccount);
    }

    @Override
    public List<AccountResponseDTO> getAllAccounts() {

        return accountRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AccountResponseDTO getAccountById(
            Long accountId) {

        Account account =
                accountRepository.findById(accountId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account not found"));

        return mapToResponse(account);
    }

    @Override
    public List<AccountResponseDTO> getAccountsByType(
            AccountType accountType) {

        return accountRepository.findByAccountType(accountType)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AccountResponseDTO> getAccountsByCustomer(
            Long customerId) {

        return accountRepository
                .findByCustomerCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AccountResponseDTO mapToResponse(
            Account account) {

        return AccountResponseDTO.builder()
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .customerName(account.getCustomer().getName())
                .branchName(account.getBranch().getBranchName())
                .ifscCode(account.getBranch().getIfscCode())
                .accountType(account.getAccountType())
                .accountStatus(account.getAccountStatus())
                .balance(account.getBalance())
                .build();
    }

    private String generateAccountNumber() {

        return "ACC" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}