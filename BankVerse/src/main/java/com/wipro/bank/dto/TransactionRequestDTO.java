package com.wipro.bank.dto;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TransactionRequestDTO {
	
	private Long senderAccountId;
	
	private Long receiverAccountId;
	
	private BigDecimal amount;
	
	private String remarks;
	

}
