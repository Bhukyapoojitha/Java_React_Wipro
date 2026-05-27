package com.wipro.BankManagementSystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "branches")
public class Branch {

    @Id
    private Long branchId;

    private String branchName;
    private String ifscCode;
    private String city;

    // Default constructor
    public Branch() {
    }

    // Parameterized constructor
    public Branch(Long branchId, String branchName, String ifscCode, String city) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.ifscCode = ifscCode;
        this.city = city;
    }

    // Getters & Setters

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // toString
    @Override
    public String toString() {
        return "Branch{" +
                "branchId=" + branchId +
                ", branchName='" + branchName + '\'' +
                ", ifscCode='" + ifscCode + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}