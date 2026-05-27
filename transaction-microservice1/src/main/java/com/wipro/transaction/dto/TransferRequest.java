package com.wipro.transaction.dto;

import lombok.Data;

@Data   // ✅ generates getters, setters automatically
public class TransferRequest {

    private String fromAccount;
    private String toAccount;
    private Double amount;
}

