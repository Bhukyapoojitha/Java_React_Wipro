package com.wipro.BankManagementSystem.service;

import java.util.List;

import com.wipro.BankManagementSystem.entity.Branch;

public interface IBranchService {

    // ✅ Create
    Branch saveBranch(Branch branch);

    // ✅ Read
    List<Branch> getAllBranches();

    Branch getBranchById(Long id);

    // ✅ Update
   // Branch updateBranch(Long id, Branch branch);

    // ✅ Delete
    //void deleteBranch(Long id);
}

