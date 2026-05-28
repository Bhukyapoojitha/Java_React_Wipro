package com.wipro.transaction.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private Long accountId;

    private String accountNumber;

    private String accountHolderName;

    private double balance;

    private String accountType;
}

