package com.wipro.bank.dto;

import java.math.BigDecimal;

import com.wipro.bank.entity.AccountType;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AccountRequestDTO {
	
	private Long customerId;
	
	private Long branchId;
	
	private AccountType accountType;
	
	private BigDecimal balance;
	

}
