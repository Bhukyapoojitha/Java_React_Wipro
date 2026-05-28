package com.wipro.bank.entity;


import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "accounts")
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId;
	
	@Column(unique = true)
	private String accountNumber;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountType accountType;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountStatus accountStatus;
	
	
	
	@Column(nullable = false)
	private BigDecimal balance;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "branch_id")
	private Branch branch;
	
	@OneToMany(mappedBy = "senderAccount",
			cascade = CascadeType.ALL)
	private List<Transaction> sentTransaction;
	
	@OneToMany(mappedBy = "receiverAccount",
	        cascade = CascadeType.ALL)
    private List<Transaction> receivedTransaction;
	

}
