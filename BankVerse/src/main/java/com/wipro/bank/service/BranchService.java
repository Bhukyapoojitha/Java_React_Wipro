package com.wipro.bank.service;

import com.wipro.bank.dto.BranchRequestDTO;
import com.wipro.bank.dto.BranchResponseDTO;

import java.util.List;

public interface BranchService {

    BranchResponseDTO createBranch(BranchRequestDTO requestDTO);

    List<BranchResponseDTO> getAllBranches();

    BranchResponseDTO getBranchById(Long branchId);

    List<?> getAccountsByBranch(Long branchId);
    
}