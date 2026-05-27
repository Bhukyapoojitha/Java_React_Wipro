package com.wipro.transaction.entity;

import jakarta.persistence.*;
import java.util.Date;

import lombok.Data;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long txnId;

    private String accountNo;

    private Double amount;

    private String txnType;   // DEPOSIT / WITHDRAW

    private String status;    // ✅ NEW FIELD (SUCCESS / FAILED)

    @Temporal(TemporalType.TIMESTAMP)
    private Date txnDate;
}
