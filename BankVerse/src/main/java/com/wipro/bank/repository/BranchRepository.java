package com.wipro.bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.bank.entity.Branch;

@Repository
public interface BranchRepository 
            extends JpaRepository<Branch, Long>{
	Optional<Branch> findByIfscCode(String ifscCode);
	
	Optional<Branch> findByBranchCode(String branchCode);
	

}
