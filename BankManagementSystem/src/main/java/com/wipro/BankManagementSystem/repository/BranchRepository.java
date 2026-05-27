package com.wipro.BankManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.BankManagementSystem.entity.Branch;


//this gives me automatic database operations without writing sql 
//without repository i have to write jdbc code, sql quiries, and connection handlings

//Controller
//GET /accounts
//Service
//return repo.findAll();
//Repository
//fetch data from DB

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

}