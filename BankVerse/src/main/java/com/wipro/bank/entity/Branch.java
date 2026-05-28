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
@Table(name = "branches")
public class Branch {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long branchId;
	
	private String branchName;
	
	@Column(unique = true)
	private String branchCode;
	
	@Column(unique = true)
	private String ifscCode;
	
	private String address;
	
	
	// one branch can have many accounts
	@OneToMany(mappedBy = "branch",
			cascade = CascadeType.ALL)
	private List<Account> accounts;
	

}
