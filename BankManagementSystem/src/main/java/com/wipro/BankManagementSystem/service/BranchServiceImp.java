package com.wipro.BankManagementSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.BankManagementSystem.entity.Branch;
import com.wipro.BankManagementSystem.repository.BranchRepository;

@Service
public class BranchServiceImp implements IBranchService {

    @Autowired
    private BranchRepository repo;

    // ✅ Save branch
    public Branch saveBranch(Branch branch) {
        return repo.save(branch);
    }

    // ✅ Get all branches
    public List<Branch> getAllBranches() {
        return repo.findAll();
    }

    // ✅ Get branch by ID
    public Branch getBranchById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // ✅ Delete branch
   // public void deleteBranch(Long id) {
   //     repo.deleteById(id);
   // }

    // ✅ Update branch
  //  public Branch updateBranch(Long id, Branch branch) {
      //  Branch existing = repo.findById(id).orElse(null);

       // if (existing != null) {
         //   existing.setBranchName(branch.getBranchName());
           // existing.setCity(branch.getCity());   // ✅ FIXED
            //existing.setIfscCode(branch.getIfscCode()); // optional but recommended
        //    return repo.save(existing);
      //  }
     //   return null;
   // }
}