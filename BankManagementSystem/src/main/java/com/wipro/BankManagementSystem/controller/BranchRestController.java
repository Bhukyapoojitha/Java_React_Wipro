package com.wipro.BankManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wipro.BankManagementSystem.entity.Branch;
import com.wipro.BankManagementSystem.service.IBranchService;

@RestController                     // ✅ VERY IMPORTANT
@RequestMapping("/branches")        // ✅ Base URL
public class BranchRestController {

    @Autowired
    private IBranchService service;

    @PostMapping
    public Branch addBranch(@RequestBody Branch branch) {
        return service.saveBranch(branch);
    }

    
    @GetMapping
    public List<Branch> getAllBranches() {
        return service.getAllBranches();
    }

    
    @GetMapping("/{id}")
    public Branch getBranchById(@PathVariable Long id) {
        return service.getBranchById(id);
    }

 }
