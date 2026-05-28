package com.wipro.bank.controller;

import com.wipro.bank.dto.*;
import com.wipro.bank.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    public ResponseEntity<BranchResponseDTO> createBranch(
            @RequestBody BranchRequestDTO requestDTO) {
        return new ResponseEntity<>(
                branchService.createBranch(requestDTO),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<BranchResponseDTO>> getAllBranches() {
        return ResponseEntity.ok(
                branchService.getAllBranches()
        );
    }

    @GetMapping("/{branchId}")
    public ResponseEntity<BranchResponseDTO> getBranchById(
            @PathVariable Long branchId) {
        return ResponseEntity.ok(
                branchService.getBranchById(branchId)
        );
    }

    @GetMapping("/{branchId}/accounts")
    public ResponseEntity<?> getAccountsByBranch(
            @PathVariable Long branchId) {
        return ResponseEntity.ok(
                branchService.getAccountsByBranch(branchId)
        );
    }
}
