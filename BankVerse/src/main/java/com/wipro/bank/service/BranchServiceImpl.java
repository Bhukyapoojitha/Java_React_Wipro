package com.wipro.bank.service;

import com.wipro.bank.dto.*;
import com.wipro.bank.entity.*;
import com.wipro.bank.exception.ResourceNotFoundException;
import com.wipro.bank.repository.*;
import com.wipro.bank.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepo;
    private final AccountRepository accountRepo;

    @Override
    public BranchResponseDTO createBranch(BranchRequestDTO dto) {
        Branch branch = Branch.builder()
                .branchName(dto.getBranchName())
                .branchCode(dto.getBranchCode())
                .ifscCode(dto.getIfscCode())
                .address(dto.getAddress())
                .build();

        return map(branchRepo.save(branch));
    }

    @Override
    public List<BranchResponseDTO> getAllBranches() {
        return branchRepo.findAll().stream().map(this::map).toList();
    }

    @Override
    public BranchResponseDTO getBranchById(Long id) {
        Branch branch = branchRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        return map(branch);
    }

    @Override
    public List<AccountResponseDTO> getAccountsByBranch(Long id) {
        return accountRepo.findByBranchBranchId(id)
                .stream()
                .map(this::mapAccount)
                .toList();
    }

    private BranchResponseDTO map(Branch b) {
        return BranchResponseDTO.builder()
                .branchId(b.getBranchId())
                .branchName(b.getBranchName())
                .branchCode(b.getBranchCode())
                .ifscCode(b.getIfscCode())
                .address(b.getAddress())
                .build();
    }

    private AccountResponseDTO mapAccount(Account a) {
        return AccountResponseDTO.builder()
                .accountId(a.getAccountId())
                .accountNumber(a.getAccountNumber())
                .customerName(a.getCustomer().getName())
                .branchName(a.getBranch().getBranchName())
                .ifscCode(a.getBranch().getIfscCode())
                .accountType(a.getAccountType())
                .accountStatus(a.getAccountStatus())
                .balance(a.getBalance())
                .build();
    }
}