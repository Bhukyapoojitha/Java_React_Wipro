package com.wipro.transaction.dto;

import lombok.Data;

@Data   // ✅ automatically creates getters & setters
public class TransactionRequest {

    private String accountNo;
    private Double amount;
}
