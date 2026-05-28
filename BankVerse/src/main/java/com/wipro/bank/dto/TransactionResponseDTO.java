package com.wipro.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.wipro.bank.entity.TransactionType;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TransactionResponseDTO {
	
	private Long transactionId;
	
	private TransactionType transactionType;
	
	private String fromAccount;
	
	private String toAccount;
	
	private BigDecimal amount;
	
	private BigDecimal updatedBalance;
	
	private BigDecimal senderBalance;
	
	private BigDecimal receiverBalance;
	
	private String message;
	
	private String remarks;
	
	private String status;
	
	private LocalDateTime transactionDate;
	

}
