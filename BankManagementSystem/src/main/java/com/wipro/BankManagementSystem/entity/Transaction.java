package com.wipro.BankManagementSystem.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private Long txnId;

    private Double amount;
    private String txnType;
    private Date txnDate;

    // Many transactions → One account
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    // Default constructor
    public Transaction() {
    }

    public Transaction(Long txnId, Double amount, String txnType, Date txnDate, Account account) {
        this.txnId = txnId;
        this.amount = amount;
        this.txnType = txnType;
        this.txnDate = txnDate;
        this.account = account;
    }

    // Getters & Setters

    public Long getTxnId() {
        return txnId;
    }

    public void setTxnId(Long txnId) {
        this.txnId = txnId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public Date getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(Date txnDate) {
        this.txnDate = txnDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "txnId=" + txnId +
                ", amount=" + amount +
                ", txnType='" + txnType + '\'' +
                ", txnDate=" + txnDate +
                '}';
    }
}

