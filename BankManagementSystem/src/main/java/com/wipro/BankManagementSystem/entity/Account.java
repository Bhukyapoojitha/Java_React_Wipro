package com.wipro.BankManagementSystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.util.Date;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private Long accountId;

    private String accountNo;
    private String accountType;
    private Double balance;
    private Date openingDate;
    private String status;

    // Relationship (Many accounts → One customer)
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // Default constructor
    public Account() {
    }

    // Parameterized constructor
    public Account(Long accountId, String accountNo, String accountType,
                   Double balance, Date openingDate, String status, Customer customer) {
        this.accountId = accountId;
        this.accountNo = accountNo;
        this.accountType = accountType;
        this.balance = balance;
        this.openingDate = openingDate;
        this.status = status;
        this.customer = customer;
    }

    // Getters & Setters

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // toString
    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", accountNo='" + accountNo + '\'' +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                ", openingDate=" + openingDate +
                ", status='" + status + '\'' +
                '}';
    }
}