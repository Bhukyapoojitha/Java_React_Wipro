package com.wipro.bank.dto;

import java.math.BigDecimal;

import com.wipro.bank.entity.AccountStatus;
import com.wipro.bank.entity.AccountType;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AccountResponseDTO {
	
	private Long accountId;
	
	private String accountNumber;
	
	private String customerName;
	
	private String branchName;
	
	private String ifscCode;
	
	private AccountType accountType;
	
	private AccountStatus accountStatus;
	
	private BigDecimal balance;
	

}
