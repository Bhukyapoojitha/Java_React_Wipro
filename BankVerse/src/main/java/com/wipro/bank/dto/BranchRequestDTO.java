package com.wipro.bank.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BranchRequestDTO {
	
	private String branchName;
	
	private String branchCode;
	
	private String ifscCode;
	
	private String address;
	

}
