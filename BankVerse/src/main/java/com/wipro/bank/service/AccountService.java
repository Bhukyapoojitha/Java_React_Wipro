package com.wipro.bank.service;

import com.wipro.bank.dto.AccountRequestDTO;
import com.wipro.bank.dto.AccountResponseDTO;
import com.wipro.bank.entity.AccountType;
import java.util.List;

public interface AccountService {

    AccountResponseDTO createAccount(AccountRequestDTO requestDTO);

    List<AccountResponseDTO> getAllAccounts();

    AccountResponseDTO getAccountById(Long accountId);

    List<AccountResponseDTO> getAccountsByType(AccountType accountType);

    List<AccountResponseDTO> getAccountsByCustomer(Long customerId);
}