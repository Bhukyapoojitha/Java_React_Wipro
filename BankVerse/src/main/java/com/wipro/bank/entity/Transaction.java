package com.wipro.bank.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transactions")
public class Transaction {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transactionId;
	
	private TransactionType transactionType;
	
	private BigDecimal amount;
	
	private String remarks;
	
	private LocalDateTime transactionDate;
	
	
	// sender transactions
	@ManyToOne
	@JoinColumn(name = "sender")
	private Account senderAccount;
	
	
	//receiver transactions
	@ManyToOne
	@JoinColumn(name = "receiver")
	private Account receiverAccount;
	
	
	

}
