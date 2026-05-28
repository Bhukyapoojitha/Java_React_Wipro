package com.wipro.bank.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerId;
	
	private String name;
	
	@Column(unique = true)
	private String email;
	
	private String phone;
	
	private String address;
	
	
	//one customer can have many accounts
	@OneToMany(mappedBy = "customer",
			cascade = CascadeType.ALL)
	private List<Account> accounts;
	

@ManyToOne
@JoinColumn(name = "branch_id")
private Branch branch;


}
